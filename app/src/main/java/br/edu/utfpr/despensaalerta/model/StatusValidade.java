package br.edu.utfpr.despensaalerta.model;

import java.time.LocalDate;

import br.edu.utfpr.despensaalerta.R;

/**
 * Níveis de urgência de um item de acordo com a sua data de validade.
 */
public enum StatusValidade {

    VENCIDO(R.string.status_vencido, R.color.status_vencido),
    VENCENDO(R.string.status_vencendo, R.color.status_vencendo),
    DENTRO_DA_VALIDADE(R.string.status_dentro_validade, R.color.status_ok);

    /** Quantidade de dias antes do vencimento em que o item passa a exigir atenção. */
    public static final int DIAS_ALERTA = 7;

    private final int rotuloRes;
    private final int corRes;

    StatusValidade(int rotuloRes, int corRes) {
        this.rotuloRes = rotuloRes;
        this.corRes = corRes;
    }

    public int getRotuloRes() {
        return rotuloRes;
    }

    public int getCorRes() {
        return corRes;
    }

    public static StatusValidade calcular(LocalDate validade, LocalDate hoje) {
        if (validade.isBefore(hoje)) {
            return VENCIDO;
        }
        if (!validade.isAfter(hoje.plusDays(DIAS_ALERTA))) {
            return VENCENDO;
        }
        return DENTRO_DA_VALIDADE;
    }
}
