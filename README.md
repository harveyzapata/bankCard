# Bank Inc Card Management API

Este proyecto es una API REST que permite gestionar tarjetas de débito o crédito para los clientes de Bank Inc. Proporciona varias funcionalidades, incluida la generación de números de tarjeta, activación de tarjetas y más.

## Endpoints

### 1. Generar número de tarjeta

- **Método**: GET
- **Recurso**: `/card/{productId}/number`
- **Descripción**: Este endpoint permite generar un número de tarjeta único a partir del ID del producto.
- **Parámetros de entrada**: `productId` (ID del producto)
- **Ejemplo de solicitud**:
  ```http
  GET /card/123456/number

  {
    "message": "Número de tarjeta generado exitosamente",
    "code": 200,
    "data": {
      "cardId": "1234567890123456",
      "cardHolderName": "John Doe",
      "expirationDate": "2026-09-02",
      "currency": "USD",
      "active": false,
      "balance": 0.0,
      "blocked": false
    }
  }


### 2. Activar Tarjeta
- **Método**: POST
- **Recurso**: `/card/enroll`
- **Descripción**: Activa una tarjeta bancaria en el sistema.
- **Ejemplo de solicitud**:
```http
POST /card/enroll

{
    "cardId": "1020301234567801"

}

```
### 3. Bloquear Tarjeta
- **Método**: DELETE
- **Recurso**: `/card/{cardId}`
- **Descripción**: Bloquea una tarjeta bancaria en el sistema..
- **Ejemplo de solicitud**:
```http
DELETE /card/1020301234567801

```
### 4. Agregar Saldo
- **Método**: POST
- **Recurso**: `/card/balance`
- **Descripción**: Agrega saldo a una tarjeta bancaria existente.
- **Ejemplo de solicitud**:
```http
POST /card/balance

{
    "cardId": "1020301234567801",
    "balance": 10000
}
```
### 5. Consulta Saldo
- **Método**: GET
- **Recurso**: `/card/balance/{cardId}`
- **Descripción**: Consulta el saldo actual de una tarjeta bancaria.
- **Ejemplo de solicitud**:
```http
GET /card/balance/1020301234567801

```
### 6. Transacción de Compra
- **Método**: GET
- **Recurso**: `/transaction/purchase`
- **Descripción**: Realiza una transacción de compra utilizando una tarjeta bancaria.
- **Ejemplo de solicitud**:
```http
POST /transaction/purchase
{
    "cardId": "1020301234567801",
    "price": 100
}

```
### 7. Obtener detalles de una transacción
- **Método:** GET
- **Ruta:** `/transaction/{transactionId}`
- **Descripción:** Esta ruta permite obtener detalles de una transacción específica mediante su ID.
```http
GET /transaction/1

```

### 8. Anular una transacción
- **Método:** POST
- **Ruta:** `/transaction/anulation`
- **Descripción:** Esta ruta permite anular una transacción previamente realizada proporcionando el numero de la tarjeta y el ID de la transacción que se desea anular en el cuerpo de la solicitud.
```http
POST /transaction/anulation
{
  "cardId": "1020301234567801",
  "transactionId": "54321"
}

