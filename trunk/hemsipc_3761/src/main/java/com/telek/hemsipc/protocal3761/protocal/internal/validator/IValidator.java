package com.telek.hemsipc.protocal3761.protocal.internal.validator;

import java.util.List;

/**
 * Created by PETER on 2015/3/17.
 */
public interface IValidator {
    boolean check(Object value, List<Double> target);
}
