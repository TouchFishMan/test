package com.telek.hemsipc.protocal3761.protocal.internal.validator;

import java.util.List;

/**
 * Created by PETER on 2015/3/17.
 */
public class Include implements IValidator {
    @Override
    public boolean check(Object value, List<Double> target) {
        double temp=Double.parseDouble(value.toString());
        for(double i:target){
            if(i==temp){
                return true;
            }
        }
        return false;
    }
}
