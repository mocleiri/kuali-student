package com.sigmasys.bsinas.gwt.server.impl;

import com.sigmasys.bsinas.annotation.UrlMapping;
import com.sigmasys.bsinas.gwt.client.model.GwtError;
import com.sigmasys.bsinas.gwt.client.service.GwtBsinasService;
import com.sigmasys.bsinas.gwt.server.AbstractRemoteService;
import com.sigmasys.bsinas.model.Constants;
import com.sigmasys.bsinas.service.BsinasService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.collegeboard.inas._2012.input.NeedAnalysisInput;
import org.collegeboard.inas._2012.output.NeedAnalysisOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * GwtBsinasService implementation
 *
 * @author Michael Ivanov
 */
@UrlMapping(Constants.BSINAS_SERVICE_URL)
@Service("gwtBsinasService")
@Transactional(readOnly = true)
public class GwtBsinasServiceImpl extends AbstractRemoteService implements GwtBsinasService {

    private static final Log logger = LogFactory.getLog(GwtBsinasServiceImpl.class);

    @Autowired
    private BsinasService bsinasService;

    @Override
    public String runEngine(String inputXml) throws GwtError {
        try {
            NeedAnalysisInput input = bsinasService.fromXml(inputXml, NeedAnalysisInput.class);
            NeedAnalysisOutput output = bsinasService.runEngine(input);
            return bsinasService.toXml(output);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
