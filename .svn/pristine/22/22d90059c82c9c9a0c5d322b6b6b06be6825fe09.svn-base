package com.telek.hemsipc.contant;  
public enum DataType {
	 	INT("int", "整数类型"),
	    FLOAT("float", "浮点类型") ;
	 	
	    private String code;
	    private String type;

	    DataType(String code, String type) {
	        this.code = code;
	        this.type = type;
	    }

	    public String getCode() {
	        return code;
	    }

	    public String getType() {
	        return type;
	    }

	    public static DataType getDataTypeByCode(String code) {
	    	DataType[] dataTypes = DataType.values();
	        for (DataType dataType : dataTypes) {
	            if (dataType.getCode().equals(code)) {
	                return dataType;
	            }
	        }
	        return null;
	    }
}
  
