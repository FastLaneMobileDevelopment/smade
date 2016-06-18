package io.bega.servicebase.model.appointment;


/**
 *
 * 1. Concretados

 2. Pendiente Informar

 3. En Revisión

 4. Aceptado (Pago a Vencimiento)

 5. Rechazado por falta información

 6. Pendiente costes sólo para empleados

 7. Rechazado por Fotos

 8. Rechazado por Coste / Alegaciones

 9. Cobrado

 10. Descuento (Esta no sale en el planning)
 *
 *
 */
public enum OrderTaskStatus {
	Concretado,
	PedienteInformar,
	EnRevision,
	RechazadoPorFaltaInformacion,
	PendienteCostes,
	RechazadoPorFotos,
	RechazadoPorCostesAlejaciones,
	Cobrado,
	Descuento
}
