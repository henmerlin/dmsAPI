/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dms.service;

import controller.in.CriarLinhaIn;
import controller.in.DeletarLinhaIn;
import controller.in.EditServIn;
import controller.in.ManobrarLinhaIn;
import dao.dms.enums.SwitchesEnum;
import exception.FalhaAoExecutarComandoDeAlteracaoException;
import java.util.ArrayList;
import java.util.List;
import model.dms.ConfiguracaoDMS;
import model.dms.ConsultaDMS;
import model.dms.ConfiguracoesShelf;
import model.dms.FacilidadesMapci;
import model.dms.dto.LineServiceDTO;

public class ServiceDMSImpl extends GenericDMSService implements ServiceDMS {

    public ServiceDMSImpl() {
    }

    @Override
    public ConfiguracaoDMS consultar(ConsultaDMS in) throws Exception {
        SwitchesEnum enu = SwitchesEnum.findByName(in.getCentral());
        return manager(enu).consultarPorDn(in.getDn());
    }

    @Override
    public ConfiguracaoDMS criarLinha(CriarLinhaIn in) throws Exception {
        SwitchesEnum enu = SwitchesEnum.findByName(in.getDms().getCentral());
        ConfiguracaoDMS linha = new ConfiguracaoDMS();
        linha.setDn(in.getDms().getDn());
        linha.setCustGrp(in.getConfBinada().getCustGrp().replaceFirst("_\\.{3}", "_POS"));
        linha.setLen(in.getLen());
        try {
            return manager(enu).criarLinha(linha);
        } catch (FalhaAoExecutarComandoDeAlteracaoException e) {
            manager(enu).abort();
            throw e;
        }

    }

    @Override
    public ConfiguracaoDMS deletarLinha(DeletarLinhaIn in) throws Exception {
        SwitchesEnum enu = SwitchesEnum.findByName(in.getDms().getCentral());
        ConfiguracaoDMS linha = new ConfiguracaoDMS();
        linha.setDn(in.getDms().getDn());
        linha.setLen(in.getLen());
        try {
            manager(enu).deletarLinha(linha);
        } catch (FalhaAoExecutarComandoDeAlteracaoException e) {
            manager(enu).abort();
            throw e;
        }
        return manager(enu).consultarPorDn(linha.getDn());

    }

    @Override
    public ConfiguracoesShelf consultarConfiguracoesShelf(ConsultaDMS in) throws Exception {
        SwitchesEnum enu = SwitchesEnum.findByName(in.getCentral());
        ConfiguracaoDMS conf = manager(enu).consultarPorDn(in.getDn());
        List<FacilidadesMapci> listarLensLivres = manager(enu).listarLensLivres(conf.getLen());
        return new ConfiguracoesShelf(listarLensLivres, conf);
    }

    @Override
    public ConfiguracaoDMS editarServicos(EditServIn in) throws Exception {
        SwitchesEnum enu = SwitchesEnum.findByName(in.getDms().getCentral());
        ConfiguracaoDMS linha = manager(enu).consultarPorDn(in.getDms().getDn());
        List<LineServiceDTO> rmv = linha.getServicos();
        List<LineServiceDTO> add = new ArrayList<>();

        in.getServices().forEach((t) -> {
            add.add(t.dto());
        });
        manager(enu).adicionarServico(linha, add);

        rmv.removeIf((t) -> {
            return in.getServices().contains(t.toEnum());
        });
        manager(enu).removerServico(linha, rmv);

        return manager(enu).consultarPorDn(in.getDms().getDn());
    }

    @Override
    public ConfiguracaoDMS manobrarLinha(ManobrarLinhaIn in) throws Exception {
        SwitchesEnum enu = SwitchesEnum.findByName(in.getDms().getCentral());
        ConfiguracaoDMS linha = manager(enu).consultarPorDn(in.getDms().getDn());
        linha.setCustGrp(in.getConfBinada().getCustGrp().replaceFirst("_\\.{3}", "_POS"));

        return manager(enu).manobrarLinha(linha, in.getLen());
    }

}
