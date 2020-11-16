package com.minevera.auxiliars;

import com.backend.groupHomeApi.model.ProductDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Pablo on 28/04/2016.
 */
public class ComparatorDates implements Comparator<ProductDate> {


    @Override
    public int compare(ProductDate lhs, ProductDate rhs) {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = formateador.parse(lhs.getDateExpired());
            Date date2 = formateador.parse(rhs.getDateExpired());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
