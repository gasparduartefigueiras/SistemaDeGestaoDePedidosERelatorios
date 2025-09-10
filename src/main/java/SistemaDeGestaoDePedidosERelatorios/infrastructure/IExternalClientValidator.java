package SistemaDeGestaoDePedidosERelatorios.infrastructure;

import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerEmail;
import SistemaDeGestaoDePedidosERelatorios.VOs.CustomerName;
import SistemaDeGestaoDePedidosERelatorios.VOs.ValidationOutcome;

public interface IExternalClientValidator {

    ValidationOutcome validate(CustomerName name, CustomerEmail email);
}