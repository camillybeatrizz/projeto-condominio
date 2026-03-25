package com.codewithus.kondo.support;

import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.AreaComumRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.ChamadoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.ContaBancariaRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.DespesaRepository;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DatabaseCleanupSupport {

    @Autowired
    protected PagamentoRepository pagamentoRepository;

    @Autowired
    protected ChamadoRepository chamadoRepository;

    @Autowired
    protected CobrancaRepository cobrancaRepository;

    @Autowired
    protected ContratoRepository contratoRepository;

    @Autowired
    protected DespesaRepository despesaRepository;

    @Autowired
    protected ContaBancariaRepository contaBancariaRepository;

    @Autowired
    protected AcessoRepository acessoRepository;

    @Autowired
    protected AreaComumRepository areaComumRepository;

    @Autowired
    protected UnidadeRepository unidadeRepository;

    @Autowired
    protected BlocoRepository blocoRepository;

    @Autowired
    protected FornecedorRepository fornecedorRepository;

    @Autowired
    protected CondominioRepository condominioRepository;

    protected void limparBaseDeTestes() {
        pagamentoRepository.deleteAll();
        chamadoRepository.deleteAll();
        cobrancaRepository.deleteAll();
        contratoRepository.deleteAll();
        despesaRepository.deleteAll();
        contaBancariaRepository.deleteAll();
        acessoRepository.deleteAll();
        areaComumRepository.deleteAll();
        unidadeRepository.deleteAll();
        blocoRepository.deleteAll();
        fornecedorRepository.deleteAll();
        condominioRepository.deleteAll();
    }
}
