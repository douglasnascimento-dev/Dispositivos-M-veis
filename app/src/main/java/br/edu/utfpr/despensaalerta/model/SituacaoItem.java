package br.edu.utfpr.despensaalerta.model;

import br.edu.utfpr.despensaalerta.R;

/**
 * Situação de um item na despensa: ainda disponível ou já baixado (consumido/descartado).
 */
public enum SituacaoItem {

    ATIVO(R.string.situacao_ativo, R.color.status_ok),
    CONSUMIDO(R.string.situacao_consumido, R.color.situacao_consumido),
    DESCARTADO(R.string.situacao_descartado, R.color.situacao_descartado);

    private final int rotuloRes;
    private final int corRes;

    SituacaoItem(int rotuloRes, int corRes) {
        this.rotuloRes = rotuloRes;
        this.corRes = corRes;
    }

    public int getRotuloRes() {
        return rotuloRes;
    }

    public int getCorRes() {
        return corRes;
    }
}
