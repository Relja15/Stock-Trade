package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.entity.Supplier;
import org.springframework.stereotype.Service;

@Service
public class ValueUtilityService {
    public static <T> T getNonEmptyValue(T newValue, T oldValue) {
        if (newValue == null) {
            return oldValue;
        }
        if (newValue instanceof Double) {
            return (Double) newValue != 0 ? newValue : oldValue;
        }
        if (newValue instanceof Integer) {
            return (Integer) newValue != 0 ? newValue : oldValue;
        }
        if (newValue instanceof String) {
            return !((String) newValue).isEmpty() ? newValue : oldValue;
        }
        if(newValue instanceof Category || newValue instanceof Supplier){
            return newValue;
        }
        return oldValue;
    }

    public static int getStringFromDtoToInt(String stringFromDto){
        return !stringFromDto.isEmpty() ? Integer.parseInt(stringFromDto) : 0;
    }

    public static double getStringFromDtoToDouble(String stringFromDto){
        return !stringFromDto.isEmpty() ? Double.parseDouble(stringFromDto) : 0;
    }
}
