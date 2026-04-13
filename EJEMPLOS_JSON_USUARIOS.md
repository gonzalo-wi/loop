# 📋 Ejemplos de JSON para Crear Usuarios

## ✅ JSON CORRECTO - Usuario Web (ADMIN)

```json
POST /loop/api/users/register

{
  "name": "Luis",
  "lastName": "Peralta",
  "username": "lperalta",
  "password": "lperalta1234",
  "sucursalId": 1,
  "role": "ADMIN"
}
```

**Campos obligatorios para usuarios web (ADMIN, USER):**
- ✅ `name` - OBLIGATORIO
- ✅ `username` - OBLIGATORIO  
- ✅ `password` - OBLIGATORIO
- ✅ `sucursalId` - OBLIGATORIO
- ✅ `role` - OBLIGATORIO
- ⚠️ `lastName` - OPCIONAL
- ⚠️ `pin` - OPCIONAL (no necesario para web)

---

## ✅ JSON CORRECTO - Usuario Móvil (DELIVERY)

```json
POST /loop/api/users/register

{
  "name": "Carlos",
  "lastName": "Gómez",
  "pin": 1234,
  "sucursalId": 1,
  "role": "DELIVERY",
  "deliveryId": 100
}
```

**Campos obligatorios para usuarios móviles (DELIVERY, CONTROLLER, PICKER):**
- ✅ `name` - OBLIGATORIO
- ✅ `pin` - OBLIGATORIO (para login móvil)
- ✅ `sucursalId` - OBLIGATORIO
- ✅ `role` - OBLIGATORIO
- ✅ `deliveryId` - OBLIGATORIO (solo para DELIVERY)
- ⚠️ `lastName` - OPCIONAL
- ⚠️ `username` - OPCIONAL (no necesario para móvil)
- ⚠️ `password` - OPCIONAL (no necesario para móvil)

---

## ✅ JSON CORRECTO - Usuario CONTROLLER

```json
POST /loop/api/users/register

{
  "name": "María",
  "lastName": "López",
  "pin": 5678,
  "sucursalId": 1,
  "role": "CONTROLLER"
}
```

---

## ✅ JSON CORRECTO - Usuario PICKER

```json
POST /loop/api/users/register

{
  "name": "Juan",
  "lastName": "Martínez",
  "pin": 9012,
  "sucursalId": 1,
  "role": "PICKER"
}
```

---

## ❌ ERRORES COMUNES

### Error 1: Falta el campo `name`
```json
{
  "username": "lperalta",
  "password": "lperalta1234",
  "sucursalId": 1,
  "role": "ADMIN"
}
```
**Error:** `"El nombre es obligatorio"`

---

### Error 2: Falta `username` para usuario web
```json
{
  "name": "Luis",
  "password": "lperalta1234",
  "sucursalId": 1,
  "role": "ADMIN"
}
```
**Error:** `"Los usuarios web (ADMIN, USER) requieren username"`

---

### Error 3: Falta `pin` para usuario móvil
```json
{
  "name": "Carlos",
  "sucursalId": 1,
  "role": "DELIVERY",
  "deliveryId": 100
}
```
**Error:** `"Los usuarios móviles (DELIVERY, CONTROLLER, PICKER) requieren PIN"`

---

### Error 4: Falta `deliveryId` para DELIVERY
```json
{
  "name": "Carlos",
  "pin": 1234,
  "sucursalId": 1,
  "role": "DELIVERY"
}
```
**Error:** `"Los usuarios de tipo DELIVERY deben tener un deliveryId"`

---

## 📝 Resumen de Validaciones

| Campo | ADMIN/USER | DELIVERY/CONTROLLER/PICKER |
|-------|------------|----------------------------|
| `name` | ✅ Obligatorio | ✅ Obligatorio |
| `lastName` | ⚠️ Opcional | ⚠️ Opcional |
| `username` | ✅ Obligatorio | ⚠️ Opcional |
| `password` | ✅ Obligatorio | ⚠️ Opcional |
| `pin` | ⚠️ Opcional | ✅ Obligatorio |
| `sucursalId` | ✅ Obligatorio | ✅ Obligatorio |
| `role` | ✅ Obligatorio | ✅ Obligatorio |
| `deliveryId` | ⚠️ No aplica | ✅ Solo para DELIVERY |

---

## 🔐 Ejemplos de Login

### Login Web (username/password)
```json
POST /loop/api/users/login

{
  "username": "lperalta",
  "password": "lperalta1234"
}
```

### Login Móvil (PIN)
```json
POST /loop/api/users/login

{
  "pin": 1234
}
```

---

## 💡 Respuesta Exitosa

```json
{
  "id": 1,
  "name": "Luis",
  "lastName": "Peralta",
  "username": "lperalta",
  "pin": null,
  "sucursalId": 1,
  "sucursalName": "Sucursal Centro",
  "role": "ADMIN",
  "deliveryId": null,
  "active": true
}
```

---

## ❌ Respuesta de Error de Validación

```json
{
  "timestamp": "2026-04-13T17:45:00",
  "status": 400,
  "errors": {
    "name": "El nombre es obligatorio",
    "sucursalId": "La sucursal es obligatoria"
  }
}
```
