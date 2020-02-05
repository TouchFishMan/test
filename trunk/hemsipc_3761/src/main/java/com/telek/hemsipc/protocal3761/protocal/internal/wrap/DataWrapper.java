package com.telek.hemsipc.protocal3761.protocal.internal.wrap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.telek.hemsipc.protocal3761.protocal.CodecConfig;
import com.telek.hemsipc.protocal3761.protocal.Packet;
import com.telek.hemsipc.protocal3761.protocal.exception.DecodingException;
import com.telek.hemsipc.protocal3761.protocal.exception.EncodingException;
import com.telek.hemsipc.protocal3761.protocal.exception.InvalidConfigException;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.Constants;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.Field;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.FieldGroup;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.FieldTypeParam;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.IFieldNode;
import com.telek.hemsipc.protocal3761.protocal.internal.ConfigParse.Validator;
import com.telek.hemsipc.protocal3761.protocal.internal.ProtocalTemplate;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldGroupFormater.FormatterImpl;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldGroupFormater.IFormatter;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldType.Da;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldType.Dt;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldType.FieldTypeContext;
import com.telek.hemsipc.protocal3761.protocal.internal.fieldType.IFieldType;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.Data;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.PacketSegmentContext;
import com.telek.hemsipc.protocal3761.protocal.internal.packetSegment.SegmentEnum;
import com.telek.hemsipc.protocal3761.protocal.internal.validator.IValidator;
import com.telek.hemsipc.protocal3761.protocal.internal.validator.ValidatorContext;


/**
 * Created by PETER on 2015/3/25.
 */
public class DataWrapper extends Wrapper implements Constants {
    private static final String MULTIPLECOMMAND="multipleCommand";
    private Da da=new Da();
    private Dt dt=new Dt();
    private FieldTypeParam dadtParam=new FieldTypeParam(2);

    private FieldTypeContext fieldTypeContext=new FieldTypeContext();
    private ValidatorContext validatorContext=new ValidatorContext();
    private IFormatter formatter=new FormatterImpl();
    private Map<String,EncodeFieldGroup> encodeFieldGroupMap=new HashMap<>();

    public DataWrapper(){
        init();
    }

    private void init(){
        encodeFieldGroupMap.put(LIST, new EncodeFieldGroup() {
            /**
             * 编码list类型group
             * @param fieldGroup
             * @param dataContentMap
             * @param buffer
             * @param protocalTemplate
             * @throws Exception
             */
            @Override
            public void execute(FieldGroup fieldGroup, Map<String,Object> dataContentMap,
                                List<byte[]> buffer, ProtocalTemplate protocalTemplate) throws Exception{
                ExpressionParser parser = new SpelExpressionParser();
                EvaluationContext context = new StandardEvaluationContext();
                context.setVariable("data", dataContentMap);
                int numRef;
                try {
                    numRef = parser.parseExpression(fieldGroup.getRefNum()).
                            getValue(context, Integer.class);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new DecodingException(1111,"list数量解析失败:"+fieldGroup.getRefNum());
                }
                List<Object> list = (List) dataContentMap.get(LIST);
                for (int i = 0; i < numRef; i++) {
                    Object manyIndex = list.get(i);
                    if(fieldGroup.getFormater()!=null && manyIndex instanceof String){
                        Map manyMapIndex=formatter.division(fieldGroup.getFormater(),manyIndex.toString());
                        encodeData(fieldGroup, manyMapIndex,buffer,protocalTemplate);
                    }else if(manyIndex instanceof Map){
                        encodeData(fieldGroup, (Map)manyIndex,buffer,protocalTemplate);
                    }else{
                        throw new EncodingException(1111,"数据格式错误"+manyIndex);
                    }
                }
            }
        });


        encodeFieldGroupMap.put(FIELDGROUP, new EncodeFieldGroup() {
            /**
             * 编码一般类型group
             * @param fieldGroup
             * @param dataContentMap
             * @param buffer
             * @param protocalTemplate
             * @throws Exception
             */
            @Override
            public void execute(FieldGroup fieldGroup, Map<String, Object> dataContentMap, List<byte[]> buffer, ProtocalTemplate protocalTemplate) throws Exception {
                Object value=dataContentMap.get(fieldGroup.getName());
                if(fieldGroup.getFormater()!=null && value instanceof String){
                    Map valueMap=formatter.division(fieldGroup.getFormater(),value.toString());
                    encodeData(fieldGroup, valueMap , buffer,protocalTemplate);
                }else if(value instanceof Map){
                    encodeData(fieldGroup, (Map)value , buffer,protocalTemplate);
                }else{
                    throw new EncodingException(1111,"数据格式错误"+value);
                }
            }
        });

        encodeFieldGroupMap.put(REFFIELDGROUP, new EncodeFieldGroup() {
            /**
             * 编码引用类型group
             * @param fieldGroup
             * @param dataContentMap
             * @param buffer
             * @param protocalTemplate
             * @throws Exception
             */
            @Override
            public void execute(FieldGroup fieldGroup, Map<String, Object> dataContentMap, List<byte[]> buffer, ProtocalTemplate protocalTemplate) throws Exception {
                String refType = fieldGroup.getRefType();
                FieldGroup refGroup = protocalTemplate.getFieldGroupMap().get(refType);
                if (refGroup == null) {
                    throw new InvalidConfigException(1111, "找不到refGroup:" + refType);
                }
                Object value = dataContentMap.get(fieldGroup.getName());

                if (refGroup.getFormater() != null) {
                    if (value instanceof String) {
                        Map valueMap = formatter.division(refGroup.getFormater(), value.toString());
                        encodeData(refGroup, valueMap, buffer, protocalTemplate);
                    } else {
                        throw new EncodingException(1111, "数据格式错误" + value);
                    }
                } else {
                    encodeData(refGroup, dataContentMap, buffer, protocalTemplate);
                }
            }
        });
    }


    /**
     * 编码数据区
     * @param in
     * @param packetSegmentContext
     * @param protocalTemplate
     * @param codecConfig
     * @throws Exception
     */
    @Override
    public void encode(Packet in, PacketSegmentContext packetSegmentContext, ProtocalTemplate protocalTemplate, CodecConfig codecConfig) throws Exception {
        Data dataSeg=(Data)packetSegmentContext.getSegment(SegmentEnum.data);
        Map<String,Object> dataContentMap=in.getData();
        List<byte[]> buffer=dataSeg.getBuffer();
        if(in.getCommand().equals(MULTIPLECOMMAND)){
            Iterator commandIter=in.getData().keySet().iterator();
            while (commandIter.hasNext()){
                String command=commandIter.next().toString();
                FieldGroup dataGroup=protocalTemplate.getDataMapByName().get(command);
                dataSeg.setFn(dataGroup.getFn());
                buffer.add(da.encode(dataSeg.getPn(), dadtParam));
                buffer.add(dt.encode(dataSeg.getFn(),dadtParam));
                if(dataGroup==null){
                    throw new EncodingException(1111,"不支持命令号:"+in.getCommand());
                }
                encodeData(dataGroup, (Map)dataContentMap.get(command), buffer, protocalTemplate);
            }


        }else{
            buffer.add(da.encode(dataSeg.getPn(),dadtParam));
            buffer.add(dt.encode(dataSeg.getFn(),dadtParam));
            dataSeg.setData(in.getData());
            FieldGroup dataGroup=protocalTemplate.getDataMapByName().get(in.getCommand());
            if(dataGroup==null){
                throw new EncodingException(1111,"不支持命令号:"+in.getCommand());
            }
            encodeData(dataGroup, dataContentMap, buffer, protocalTemplate);
        }

        if(next!=null){
            next.encode(in,packetSegmentContext,protocalTemplate,codecConfig);
        }

    }


    /**
     * 解码字段组
     * @param dataGroup
     * @param dataContentMap
     * @param buffer
     * @param protocalTemplate
     * @throws Exception
     */
    private void encodeData(FieldGroup dataGroup,Map<String,Object> dataContentMap,
                            List<byte[]> buffer,ProtocalTemplate protocalTemplate) throws Exception{
        //如果是仅编码fieldgroup 则跳过
        if (dataGroup.getDirection() == 2) {
            return;
        }
        Iterator dataIt=dataGroup.getChildNodes().entrySet().iterator();

        while (dataIt.hasNext()) {
            Map.Entry entry = (Map.Entry) dataIt.next();
            IFieldNode IFieldNode = (IFieldNode) entry.getValue();
            String name = (String) entry.getKey();
            if (IFieldNode instanceof Field) {
                Field field = (Field) IFieldNode;
                if (field.getDirection() == 2) {
                    continue;
                }
                encodeField(name, field, dataContentMap, buffer);
            }
            //解码group，递归解码
            if (IFieldNode instanceof FieldGroup) {
                FieldGroup fieldGroup = (FieldGroup) IFieldNode;
                if (fieldGroup.getDirection() == 2) {
                    continue;
                }
                EncodeFieldGroup encodeFieldGroup=
                        encodeFieldGroupMap.get(fieldGroup.getType());
                if(encodeFieldGroup!=null){
                    encodeFieldGroup.execute(fieldGroup,dataContentMap,buffer,protocalTemplate);
                }

            }
        }
    }


    /**
     * 解码字段
     * @param name
     * @param field
     * @param dataContentMap
     * @param buffer
     * @throws Exception
     */
    private void encodeField(String name,Field field,Map<String,Object> dataContentMap,List<byte[]> buffer) throws Exception{
        IFieldType fieldType =fieldTypeContext.get(field.getFieldType());
        if(fieldType==null){
            throw new InvalidConfigException(1111,"找不到fieldType:"+field.getName());
        }
        Object value=dataContentMap.get(name);
        if(value==null){
            value=field.getDefaultValue();
            if(value==null){
                throw new EncodingException(1111,name+"字段值为空");
            }
        }
        if(field.getValidators()!=null){
            for(Validator v:field.getValidators()){
                IValidator validator=validatorContext.get(v.getName());
                if(!validator.check(value,v.getTarget())){
                    throw new EncodingException(111,name+"字段值 <"+value+">无效");
                }
            }
        }
        buffer.add(fieldType.encode(value, field.getFieldTypeParam()));
    }


    /**
     * 回调接口
     */
    private interface EncodeFieldGroup{
        void execute(FieldGroup fieldGroup, Map<String, Object> dataContentMap,
                     List<byte[]> buffer, ProtocalTemplate protocalTemplate) throws Exception;
    }
}
