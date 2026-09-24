package br.edu.utfpr.despensaalerta.ui.itens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import br.edu.utfpr.despensaalerta.R;
import br.edu.utfpr.despensaalerta.data.ItemRepository;
import br.edu.utfpr.despensaalerta.model.Item;
import br.edu.utfpr.despensaalerta.ui.adapter.ItemAdapter;
import br.edu.utfpr.despensaalerta.ui.item.DetalheItemActivity;

/**
 * Lista de todos os itens disponíveis, com busca por nome e filtro por categoria.
 */
public class ItensFragment extends Fragment {

    private TextInputEditText campoBusca;
    private MaterialAutoCompleteTextView campoCategoria;
    private TextView textVazio;
    private ItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itens, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        campoBusca = view.findViewById(R.id.campo_busca);
        campoCategoria = view.findViewById(R.id.campo_filtro_categoria);
        textVazio = view.findViewById(R.id.text_vazio);

        adapter = new ItemAdapter(false, item ->
                startActivity(DetalheItemActivity.abrir(requireContext(), item.getId())));
        RecyclerView lista = view.findViewById(R.id.recycler_itens);
        lista.setAdapter(adapter);

        if (campoCategoria.getText().length() == 0) {
            campoCategoria.setText(getString(R.string.filtro_todas_categorias), false);
        }
        campoCategoria.setOnItemClickListener((parent, v, position, id) -> atualizar());
        campoBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                atualizar();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizar();
    }

    private void atualizar() {
        String texto = campoBusca.getText() == null ? "" : campoBusca.getText().toString();
        String categoria = campoCategoria.getText().toString();
        if (categoria.equals(getString(R.string.filtro_todas_categorias))) {
            categoria = null;
        }

        List<Item> itens = ItemRepository.getInstance().buscar(texto, categoria);
        adapter.submeter(itens);
        textVazio.setVisibility(itens.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
