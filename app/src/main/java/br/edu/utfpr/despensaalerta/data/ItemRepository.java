package br.edu.utfpr.despensaalerta.data;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.edu.utfpr.despensaalerta.model.Item;
import br.edu.utfpr.despensaalerta.model.SituacaoItem;
import br.edu.utfpr.despensaalerta.model.StatusValidade;

/**
 * Ponto único de acesso aos itens da despensa.
 * <p>
 * Nesta versão (Entrega Parcial 2) os dados ficam apenas em memória, com alguns itens de
 * exemplo para demonstrar as telas. Na próxima etapa a implementação interna será trocada
 * pelo banco local (SQLite/Room) sem alterar a interface pública usada pelas telas.
 */
public final class ItemRepository {

    private static ItemRepository instancia;

    private final List<Item> itens = new ArrayList<>();
    private long proximoId = 1;

    public static synchronized ItemRepository getInstance() {
        if (instancia == null) {
            instancia = new ItemRepository();
        }
        return instancia;
    }

    private ItemRepository() {
        carregarDadosExemplo();
    }

    private void carregarDadosExemplo() {
        LocalDate hoje = LocalDate.now();
        inserir(new Item("Leite integral", "Laticínios", 2, "Geladeira", hoje.minusDays(2)));
        inserir(new Item("Iogurte natural", "Laticínios", 4, "Geladeira", hoje.plusDays(3)));
        inserir(new Item("Peito de frango", "Carnes", 1, "Freezer", hoje.plusDays(45)));
        inserir(new Item("Arroz branco 5kg", "Grãos", 1, "Armário", hoje.plusDays(180)));
        inserir(new Item("Feijão carioca", "Grãos", 2, "Armário", hoje.plusDays(6)));
        inserir(new Item("Detergente", "Limpeza", 3, "Área de serviço", hoje.plusDays(365)));
        inserir(new Item("Pasta de dente", "Higiene", 1, "Banheiro", hoje.minusDays(10)));

        long pao = inserir(new Item("Pão de forma", "Padaria", 1, "Armário", hoje.minusDays(1)));
        registrarBaixa(pao, SituacaoItem.DESCARTADO);
        long queijo = inserir(new Item("Queijo mussarela", "Laticínios", 1, "Geladeira", hoje.plusDays(5)));
        registrarBaixa(queijo, SituacaoItem.CONSUMIDO);
    }

    /** Itens ainda disponíveis, do vencimento mais próximo para o mais distante. */
    public synchronized List<Item> listarAtivos() {
        List<Item> resultado = new ArrayList<>();
        for (Item item : itens) {
            if (item.isAtivo()) {
                resultado.add(item.copiar());
            }
        }
        resultado.sort(Comparator.comparing(Item::getDataValidade));
        return resultado;
    }

    /**
     * Busca itens ativos pelo nome e, opcionalmente, por categoria e status de validade.
     *
     * @param texto     trecho do nome (ignora maiúsculas e acentos); vazio para todos
     * @param categoria categoria exata ou {@code null} para todas
     * @param status    status de validade ou {@code null} para todos
     */
    public synchronized List<Item> buscar(String texto, String categoria, StatusValidade status) {
        String termo = normalizar(texto);
        List<Item> resultado = new ArrayList<>();
        for (Item item : listarAtivos()) {
            boolean nomeConfere = termo.isEmpty() || normalizar(item.getNome()).contains(termo);
            boolean categoriaConfere = categoria == null || categoria.equals(item.getCategoria());
            boolean statusConfere = status == null || item.getStatus() == status;
            if (nomeConfere && categoriaConfere && statusConfere) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public synchronized List<Item> listarPorStatus(StatusValidade status) {
        List<Item> resultado = new ArrayList<>();
        for (Item item : listarAtivos()) {
            if (item.getStatus() == status) {
                resultado.add(item);
            }
        }
        return resultado;
    }

    public synchronized int contarPorStatus(StatusValidade status) {
        return listarPorStatus(status).size();
    }

    /** Itens consumidos ou descartados, da baixa mais recente para a mais antiga. */
    public synchronized List<Item> listarHistorico() {
        List<Item> resultado = new ArrayList<>();
        for (Item item : itens) {
            if (!item.isAtivo()) {
                resultado.add(item.copiar());
            }
        }
        resultado.sort(Comparator.comparing(Item::getDataBaixa).reversed());
        return resultado;
    }

    public synchronized Item buscarPorId(long id) {
        Item item = localizar(id);
        return item == null ? null : item.copiar();
    }

    public synchronized long inserir(Item item) {
        Item novo = item.copiar();
        novo.setId(proximoId++);
        novo.setSituacao(SituacaoItem.ATIVO);
        novo.setDataBaixa(null);
        itens.add(novo);
        return novo.getId();
    }

    public synchronized boolean atualizar(Item item) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId() == item.getId()) {
                itens.set(i, item.copiar());
                return true;
            }
        }
        return false;
    }

    public synchronized boolean excluir(long id) {
        return itens.removeIf(item -> item.getId() == id);
    }

    public synchronized boolean registrarBaixa(long id, SituacaoItem situacao) {
        Item item = localizar(id);
        if (item == null || !item.isAtivo() || situacao == SituacaoItem.ATIVO) {
            return false;
        }
        item.setSituacao(situacao);
        item.setDataBaixa(LocalDate.now());
        return true;
    }

    private Item localizar(long id) {
        for (Item item : itens) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String semAcentos = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcentos.toLowerCase(Locale.ROOT);
    }
}
