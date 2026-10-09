# Calculadora Estadistica XML

Se requiere el desarrollo de una aplicación Java para calcular estadísticas de variables numéricas almacenadas en un documento XML.

La aplicación debe recibir a través de la línea de comandos el nombre de un documento XML y al menos el nombre de una variable.

Para la variable se debe calcular:
- Media
- Desviación estándar
- Varianza
- Valor máximo
- Valor mínimo 

Si el usuario provee el nombre de dos variables, la aplicación debe calcular todas las estadísticas anteriores para cada variable y adicionalmente el coeficiente de correlación entre las dos variables.

La aplicación puede asumir qué el documento XML está bien formado. Sin embargo, debe realizar un manejo efectivo de situaciones excepcionales y errores. Por ejemplo:

- No se puede abrir el documento XML
- El usuario se equivoca en el nombre de la variable o la variable no existe en el documento.
- La variable qué seleccionó el usuario no es una variable numérica.

Debe incluir el uso de al menos una clase del Java Collections API.
