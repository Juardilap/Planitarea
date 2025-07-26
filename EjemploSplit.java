public class EjemploSplit {
    public static void main(String[] args) {
        String texto = "Manzana,Plátano,Uva,Piña";
        
        // Dividir la cadena en subcadenas utilizando la coma como delimitador
        String[] frutas = texto.split(",");
        
        for (String fruta : frutas) {
            System.out.println(fruta);
        }
    }
}
