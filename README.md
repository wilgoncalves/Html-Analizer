## Implementação do Analisador de HTML para Extração de Texto no Nível Mais Profundo

Este projeto consiste em um programa Java que analisa o conteúdo HTML de uma página web, identifica o texto localizado no nível mais profundo da estrutura de tags e verifica se o HTML está bem formado. A solução foi desenvolvida seguindo requisitos específicos e trata casos de erro, como HTML malformado ou falhas de conexão com a URL.

### *Funcionamento do Código*
**1. Entrada e Validação Inicial**

* O programa é executado via linha de comando com o argumento:

```
java HtmlAnalyzer [URL]
```
* Se nenhuma URL for fornecida, exibe a mensagem "URL não fornecida." e encerra.

**2. Conexão HTTP e Leitura do HTML**

* Utiliza HttpURLConnection para fazer uma requisição GET à URL especificada.

* Se a conexão falhar (código de resposta diferente de HTTP_OK), lança a exceção "URL connection error".

* O conteúdo HTML é lido inteiramente e dividido em linhas para processamento.

**3. Processamento das Linhas HTML**
Cada linha é processada conforme seu tipo:

* Tag de Abertura (ex: &lt;div&gt;):

    *1. Adiciona o nome da tag (sem < e >) em uma lista de controle (tagsAbertura).*
    
    *2. Incrementa o nível de profundidade (profundidade++).*
  

* Tag de Fechamento (ex: &lt;/div&gt;):

    *1. Verifica se corresponde à última tag aberta (comparando com o último elemento de tagsAbertura).*
    
    *2. Se válida, remove a tag da lista e decrementa a profundidade (profundidade--).*
    
    *3. Se inválida (tags não correspondentes ou fechamento sem abertura), lança "malformed HTML".*


* Texto Puro (ex: "Conteúdo exemplo"):

    *1. Armazena o texto em um Mapa (profundidadesTextos), associando-o ao nível de profundidade atual.*

**4. Validação Final e Saída**
* Após processar todas as linhas:

    *1. Se a lista tagsAbertura não estiver vazia, significa que há tags não fechadas → "malformed HTML".*

    *2. Caso contrário, o texto no maior nível de profundidade (chave mais alta no mapa) é exibido.*

### Métodos Auxiliares

* fetchUrlContent(String url):

    *1. Realiza a conexão HTTP e retorna o conteúdo da URL como String.*
    
    *2. Lança IOException em caso de falha na conexão.*

* isTagAbertura(String tag) / isTagFechamento(String tag):

    *1. Verificam se uma linha é uma tag de abertura ou fechamento, respectivamente, baseando-se nos caracteres '<', '>' e '/'.*

* removerCaracteresTags(String tag):

    *1. Remove os símbolos das tags (ex: transforma &lt;div&gt; em div) para comparação.*

### Tratamento de Erros
* HTML Malformado:

    *1. Tags de fechamento inesperadas ou ausência de fechamento → "malformed HTML".*

* Falha de Conexão:

    *1. Erros de rede ou URL inválida → "URL connection error".*

### Exemplo de Saída
* Caso de Sucesso (HTML bem formado):
```
Texto no nível mais profundo.
```
* Caso de Erro:
```
malformed HTML
```
ou
```
URL connection error
```

### Observações Técnicas
* Eficiência: O algoritmo percorre o HTML uma única vez (O(n)), usando estruturas de dados simples (ArrayList, HashMap).

* Restrições:

    *1. Não suporta tags sem fechamento (ex: &lt;br&gt;) ou com atributos (ex: &lt;div id="x"&gt;).*
    
    *2. Linhas com múltiplos tipos de conteúdo são consideradas inválidas.*
