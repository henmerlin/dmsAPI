/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dms.impl.filter;

import java.util.ArrayList;
import java.util.List;
import model.dms.FacilidadesMapci;
import model.dms.Len;

public class FilterLen implements Filter<FacilidadesMapci> {

    private Len len;

    public FilterLen(Len len) {
        this.len = len;
    }

    @Override
    public List<FacilidadesMapci> filter(List<FacilidadesMapci> lst) {
        List<FacilidadesMapci> ret = new ArrayList<>();
        lst.stream().filter((c) -> (c.getLen().equals(this.len))).forEachOrdered((c) -> {
            ret.add(c);
        });
        return ret;
    }

}
