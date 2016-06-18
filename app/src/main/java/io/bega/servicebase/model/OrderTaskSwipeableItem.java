package io.bega.servicebase.model;

import org.joda.time.DateTime;

import io.bega.servicebase.model.appointment.OrderTaskStatus;
import io.bega.servicebase.model.items.SwipeableItem;

/**
 * Created by user on 4/22/16.
 */
public class OrderTaskSwipeableItem extends SwipeableItem  {

    public long Id;

    public DateTime Date;

    // Calle JACINT VERDAGUER 11 BAJO 3ª - 08170 Montornes del Vallès (Vallès Oriental)
    public String Location;

    // Cliente: AIDE
    public String Customer;

    public DateTime EnterTime;

    public DateTime ExitTime;

    public OrderTaskStatus Status;

    // Example: Expediente: ZAQ4941
    public String InternalRecordID;

    // Expediente (Cía): ZAQ4941
    public String CompanyRercordID;

    // Cía: BS SEGUROS
    public String Company;

    // Contrato: PROTECCION HOGAR BASIC
    public String ContractName;

    // Teléfonos: 610579257
    public String Telephone;

    // Asegurado: ROSARIO OLIVER PEREZ - 38418747F
    public String Assurance;

    // ROGAMOS ENVIO DE REPARADOR POR POSIBLE ROTURA DE TUBERIA CAUSA HUMEDAD EN PARED DE LA COCINA, GRACIAS, calle JACINT VERDAGUER, 08170 - MONTORNES DEL VALLES - Barcelona - ESPAÑA Nº11 BAJO 3ª
    public String Request;

    public String idtask;

    private OrderTaskSwipeableItem()
    {

    }

    public OrderTaskSwipeableItem(long id, String idtask, DateTime date, String location, String customer, OrderTaskStatus status)
    {
        this.idtask = idtask;
        this.Id = id;
        this.Date  = date;
        this.Location = location;
        this.Customer = customer;
        this.Status = status;
    }
}
