package com.minevera.auxiliars;

import com.backend.groupHomeApi.model.ProductHome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Pablo on 28/04/2016.
 */
public class ComparadorDatesProductHome implements Comparator<ProductHome> {
    @Override
    public int compare(ProductHome lhs, ProductHome rhs) {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = formateador.parse(lhs.getInfoUnits().get(0).getDateExpired());
            Date date2 = formateador.parse(rhs.getInfoUnits().get(0).getDateExpired());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
