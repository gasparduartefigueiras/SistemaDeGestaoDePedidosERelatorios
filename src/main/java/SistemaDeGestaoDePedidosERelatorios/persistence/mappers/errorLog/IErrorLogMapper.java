package SistemaDeGestaoDePedidosERelatorios.persistence.mappers.errorLog;

import SistemaDeGestaoDePedidosERelatorios.domain.errorLog.ErrorLog;
import SistemaDeGestaoDePedidosERelatorios.persistence.dataModels.errorLog.ErrorLogDataModel;

public interface IErrorLogMapper {

    ErrorLogDataModel toDataModel(ErrorLog errorLog);

    ErrorLog toDomain(ErrorLogDataModel dataModel);
}