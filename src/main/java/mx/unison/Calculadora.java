package mx.unison;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Calculadora {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No se proporcionaron argumentos requeridos");
            System.exit(1);
        }

        if (args.length == 1) {
            System.out.println("Debes incluir al menos el nombre de una variable");
            System.exit(2);
        }

        String archivoXml = args[0];
        String var1 = args[1];
        String var2 = (args.length > 2) ? args[2] : null;

        try {
            // Cargar el documento XML
            File xmlFile = new File(archivoXml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Procesar la primera variable
            List<Double> datosVar1 = extraerDatos(doc, var1);
            System.out.println("=== Estadísticas para: " + var1 + " ===");
            imprimirEstadisticas(datosVar1);

            // Procesar la segunda variable y calcular correlación si existe
            if (var2 != null) {
                List<Double> datosVar2 = extraerDatos(doc, var2);
                System.out.println("\n=== Estadísticas para: " + var2 + " ===");
                imprimirEstadisticas(datosVar2);

                System.out.println("\n=== Análisis de Correlación ===");
                calcularEImprimirCorrelacion(datosVar1, datosVar2, var1, var2);
            }

        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.err.println("Error: No se pudo abrir, leer o parsear el documento XML ('" + archivoXml + "').");
            System.err.println("Detalle técnico: " + e.getMessage());
            System.exit(3);
        } catch (IllegalArgumentException e) {
            System.err.println("Error en los datos: " + e.getMessage());
            System.exit(4);
        }
    }

    private static List<Double> extraerDatos(Document doc, String nombreVariable) {
        NodeList nodos = doc.getElementsByTagName(nombreVariable);
        List<Double> valores = new ArrayList<>(); // Uso del Java Collections API

        if (nodos.getLength() == 0) {
            throw new IllegalArgumentException("La variable '" + nombreVariable + "' no existe en el documento o está mal escrita.");
        }

        for (int i = 0; i < nodos.getLength(); i++) {
            Node nodo = nodos.item(i);
            String texto = nodo.getTextContent().trim();
            try {
                valores.add(Double.parseDouble(texto));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El valor '" + texto + "' en la variable '" + nombreVariable + "' no es un número válido.");
            }
        }
        return valores;
    }

    private static void imprimirEstadisticas(List<Double> datos) {
        int n = datos.size();
        double suma = 0;

        for (double num : datos) {
            suma += num;
        }

        double media = suma / n;
        double min = Collections.min(datos);
        double max = Collections.max(datos);

        double sumaDiferenciasCuadrado = 0;
        for (double num : datos) {
            sumaDiferenciasCuadrado += Math.pow(num - media, 2);
        }

        double varianza = sumaDiferenciasCuadrado / n;
        double desviacionEstandar = Math.sqrt(varianza);

        System.out.printf("  N (Observaciones)   : %d%n", n);
        System.out.printf("  Media               : %.4f%n", media);
        System.out.printf("  Varianza            : %.4f%n", varianza);
        System.out.printf("  Desviación Estándar : %.4f%n", desviacionEstandar);
        System.out.printf("  Valor Mínimo        : %.4f%n", min);
        System.out.printf("  Valor Máximo        : %.4f%n", max);
    }

    private static void calcularEImprimirCorrelacion(List<Double> x, List<Double> y, String var1, String var2) {
        if (x.size() != y.size()) {
            System.err.println("  No se puede calcular la correlación: Las variables '" + var1 + "' (n=" + x.size() + ") y '" + var2 + "' (n=" + y.size() + ") tienen un número distinto de registros.");
            return;
        }

        int n = x.size();
        double sumaX = 0, sumaY = 0;

        for (int i = 0; i < n; i++) {
            sumaX += x.get(i);
            sumaY += y.get(i);
        }

        double mediaX = sumaX / n;
        double mediaY = sumaY / n;

        double covarianza = 0;
        double sumaCuadradosX = 0;
        double sumaCuadradosY = 0;

        for (int i = 0; i < n; i++) {
            double difX = x.get(i) - mediaX;
            double difY = y.get(i) - mediaY;

            covarianza += (difX * difY);
            sumaCuadradosX += Math.pow(difX, 2);
            sumaCuadradosY += Math.pow(difY, 2);
        }

        double denominador = Math.sqrt(sumaCuadradosX * sumaCuadradosY);

        if (denominador == 0) {
            System.out.println("  Coeficiente de Pearson: Indefinido (una o ambas variables son constantes).");
        } else {
            double correlacion = covarianza / denominador;
            System.out.printf("  Coeficiente de Pearson (r): %.4f%n", correlacion);
        }
    }
}