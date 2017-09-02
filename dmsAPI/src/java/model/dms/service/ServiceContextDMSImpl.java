/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dms.service;

import java.util.ArrayList;
import java.util.List;
import model.dms.dto.DetailDTO;

public class ServiceContextDMSImpl extends GenericDMSService implements ServiceContextDMS {

    @Override
    public List<DetailDTO> contextDetail() {
        List<DetailDTO> as = new ArrayList<>();
        context().getSwitchs().forEach((aSwitch) -> {
            as.add(aSwitch.getDetail());
        });
        return as;
    }

    @Override
    public void connect() {
        context().getSwitchs().forEach((t) -> {
            t.connect();
        });
    }

    @Override
    public void disconnect() {
        context().getSwitchs().forEach((t) -> {
            t.disconnect();
        });
    }

}
