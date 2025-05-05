import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("URL não fornecida.");
            return;
        }

        String url = args[0];
        Map<Integer, String> profundidadesTextos = new HashMap<>();
        // Lista para adicionar todas as tags de abertura encontradas no HTML e, 
        // assim, compará-las com as tags de fechamento.
        List<String> tagsAbertura = new ArrayList<>();

        try {
            String retornoUrl = fetchUrlContent(url);

            int profundidade = 0;
            String[] linhas = retornoUrl.split("\n");
            for (String linha : linhas) {
                String linhaSemEspaco = linha.trim();
                // Validando se é uma tag de abertura, caso positivo, a linha é adicionada
                // à lista "tagsAbertura" e somamos +1 à variável profundidade.
                if (isTagAbertura(linhaSemEspaco)) {
                    tagsAbertura.add(removerCaracteresTags(linhaSemEspaco));
                    profundidade++;
                // Se é uma tag de fechamento e corresponde a uma mesma tag de abertura, é subtraído -1 da profundidade.
                // Assim é possível contar os níveis das tags.
                } else if (isTagFechamento(linhaSemEspaco)) {
                    String nomeTag = removerCaracteresTags(linhaSemEspaco);
                    // Variável que representa o último elemento da lista tagsAbertura, 
                    // com isso podemos verificar se a tag de fechamento corresponde à tag de abertura.
                    String ultimaTagListaAbertura = tagsAbertura.get(tagsAbertura.size() - 1);
                    if (tagsAbertura.contains(nomeTag) && nomeTag.equals(ultimaTagListaAbertura)) {
                        tagsAbertura.remove(nomeTag);
                        profundidade--;
                    } else {
                        throw new Exception("Malformed HTML");
                    }
                } else {
                    profundidadesTextos.put(profundidade, linhaSemEspaco);
                }
            }

            // Se ainda houver elementos na lista, significa que a solução possui estruturas mal-formadas.
            if (!tagsAbertura.isEmpty()) {
                System.out.println("malformed HTML");
            } else {
                int maiorProfundidade = Collections.max(profundidadesTextos.keySet());
                System.out.println(profundidadesTextos.get(maiorProfundidade));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressWarnings("deprecation")
	private static String fetchUrlContent(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } else {
            throw new IOException("URL connection error");
        }
    }

    private static boolean isTagAbertura(String tag) {
        return !tag.isEmpty() && tag.charAt(0) == '<' 
        && tag.charAt(1) != '/' && tag.charAt(tag.length() - 1) == '>';
    }

    private static boolean isTagFechamento(String tag) {
        return !tag.isEmpty() && tag.charAt(0) == '<' 
        && tag.charAt(1) == '/' && tag.charAt(tag.length() - 1) == '>';
    }

    private static String removerCaracteresTags(String tag) {
        String textoTag = "";
        if (isTagAbertura(tag)) {
            textoTag = tag.substring(1, tag.length() - 1);
        } else if (isTagFechamento(tag)) {
            textoTag = tag.substring(2, tag.length() - 1);
        }
        return textoTag;
    }
}