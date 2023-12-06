package com.francisconicolau.pruebainditex.application.service.impl;

import com.francisconicolau.pruebainditex.domain.model.Status;
import com.francisconicolau.pruebainditex.infrastructure.config.ServiceProperties;
import com.francisconicolau.pruebainditex.infrastructure.config.ServicePropertyConst;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BaseService {


    @Getter
    @Autowired
    public ServiceProperties serviceProperties;

    public Status buildSuccessStatus() {
        return new Status(ServicePropertyConst.STATUS_OK, serviceProperties.getStatusMessage(ServicePropertyConst.STATUS_OK));
    }

    public Status buildUnExpectedStatus() {
        return new Status(ServicePropertyConst.STATUS_ERROR_INESPERADO, serviceProperties.getStatusMessage(ServicePropertyConst.STATUS_ERROR_INESPERADO));
    }

    public Status buildStatusFromCode(final int code) {
        return new Status(code, serviceProperties.getStatusMessage(code));
    }

    public Status buildDynamicStatusFromCode(final int code, final Integer[] ids) {
        return new Status(code, (ids != null && ids.length != 0) ? String.format(serviceProperties.getStatusMessage(code), ids) : serviceProperties.getStatusMessage(code));
    }

}
