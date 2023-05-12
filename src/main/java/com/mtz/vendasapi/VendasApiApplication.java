package com.mtz.vendasapi;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mtz.vendasapi.domain.model.Cliente;
import com.mtz.vendasapi.domain.model.Pedido;
import com.mtz.vendasapi.domain.model.Produto;
import com.mtz.vendasapi.domain.model.Usuario;
import com.mtz.vendasapi.domain.repository.ClienteRepository;
import com.mtz.vendasapi.domain.repository.PedidoRepository;
import com.mtz.vendasapi.domain.repository.ProdutoRepository;
import com.mtz.vendasapi.domain.repository.UsuarioRepository;

@SpringBootApplication
public class VendasApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendasApiApplication.class, args);
    }

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void inserirRegistros() {

        // Criação de alguns produtos
        Produto produto1 = new Produto(null, "Produto 1", "Desc 1", new BigDecimal(10.00), 1);
        Produto produto2 = new Produto(null, "Produto 2", "Desc 2", new BigDecimal(20.00), 2);
        Produto produto3 = new Produto(null, "Produto 3", "Desc 3", new BigDecimal(30.00), 3);
        Produto produto4 = new Produto(null, "Produto 4", "Desc 4", new BigDecimal(40.00), 1);
        Produto produto5 = new Produto(null, "Produto 5", "Desc 5", new BigDecimal(50.00), 3);
        Produto produto6 = new Produto(null, "Produto 6", "Desc 6", new BigDecimal(60.00), 15);
        Produto produto7 = new Produto(null, "Produto 7", "Desc 7", new BigDecimal(70.00), 82);
        Produto produto8 = new Produto(null, "Produto 8", "Desc 8", new BigDecimal(80.00), 35);
        Produto produto9 = new Produto(null, "Produto 9", "Desc 9", new BigDecimal(90.00), 46);
        Produto produto10 = new Produto(null, "Produto 10", "Desc 12", new BigDecimal(100.00), 50);
        Produto produto11 = new Produto(null, "Produto 11", "Desc 11", new BigDecimal(110.00), 60);
        Produto produto12 = new Produto(null, "Produto 12", "Desc 12", new BigDecimal(120.00), 10);
        Produto produto13 = new Produto(null, "Produto 13", "Desc 13", new BigDecimal(130.00), 52);
        Produto produto14 = new Produto(null, "Produto 14", "Desc 14", new BigDecimal(140.00), 6);

        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3, produto4, produto5, produto6, produto7,
                produto8, produto9, produto10, produto11, produto12, produto13, produto14));

        // Adiciona os produtos ao primeiro pedido
        Set<Produto> produtosPedido1 = new HashSet<>();
        produtosPedido1.add(produto1);
        produtosPedido1.add(produto2);

        // Adiciona os produtos ao segundo pedido
        Set<Produto> produtosPedido2 = new HashSet<>();
        produtosPedido2.add(produto1);
        produtosPedido2.add(produto3);

        // Adiciona os produtos ao terceiro pedido
        Set<Produto> produtosPedido3 = new HashSet<>();
        produtosPedido3.add(produto2);
        produtosPedido3.add(produto3);

        Usuario usuario1 = new Usuario();
        usuario1.setNome("João");
        usuario1.setEmail("joao@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setDataCriacao(new Date());
        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("João");
        usuario2.setEmail("joao@gmail.com");
        usuario2.setSenha("123456");
        usuario2.setDataCriacao(new Date());
        usuarioRepository.save(usuario2);

        Usuario usuario3 = new Usuario();
        usuario3.setNome("João");
        usuario3.setEmail("joao@gmail.com");
        usuario3.setSenha("123456");
        usuario3.setDataCriacao(new Date());
        usuarioRepository.save(usuario3);

        Usuario usuario4 = new Usuario();
        usuario4.setNome("João");
        usuario4.setEmail("joao@gmail.com");
        usuario4.setSenha("123456");
        usuario4.setDataCriacao(new Date());
        usuarioRepository.save(usuario4);

        Cliente cliente1 = new Cliente();
        cliente1.setNome("João");
        cliente1.setEmail("joao@gmail.com");
        cliente1.setSobrenome("Silva");
        cliente1.setSexo("Masculino");
        cliente1.setDataNascimento(new Date(1990, 5, 15));
        cliente1.setNacionalidade("Brasileiro");
        cliente1.setEndereco("Rua A, 123");
        cliente1.setCidade("São Paulo");
        cliente1.setEstado("SP");
        cliente1.setTelefone("(11) 1234-5678");
        clienteRepository.save(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria");
        cliente2.setEmail("maria@gmail.com");
        cliente2.setSobrenome("Silveira");
        cliente2.setSexo("Feminino");
        cliente2.setDataNascimento(new Date(1985, 8, 20));
        cliente2.setNacionalidade("Brasileira");
        cliente2.setEndereco("Rua B, 456");
        cliente2.setCidade("Rio de Janeiro");
        cliente2.setEstado("RJ");
        cliente2.setTelefone("(21) 9876-5432");
        clienteRepository.save(cliente2);

        Cliente cliente3 = new Cliente();
        cliente3.setNome("João");
        cliente3.setEmail("joao@gmail.com");
        cliente3.setSobrenome("Santos");
        cliente3.setSexo("Masculino");
        cliente3.setDataNascimento(new Date(1990, 5, 15));
        cliente3.setNacionalidade("Brasileira");
        cliente3.setEndereco("Rua C, 789");
        cliente3.setCidade("São Paulo");
        cliente3.setEstado("SP");
        cliente3.setTelefone("(11) 9876-5432");
        clienteRepository.save(cliente3);

        Cliente cliente4 = new Cliente();
        cliente4.setNome("Ana");
        cliente4.setEmail("ana@gmail.com");
        cliente4.setSobrenome("Silva");
        cliente4.setSexo("Feminino");
        cliente4.setDataNascimento(new Date(1987, 2, 10));
        cliente4.setNacionalidade("Brasileira");
        cliente4.setEndereco("Rua D, 123");
        cliente4.setCidade("Curitiba");
        cliente4.setEstado("PR");
        cliente4.setTelefone("(41) 9876-5432");
        clienteRepository.save(cliente4);

        Cliente cliente5 = new Cliente();
        cliente5.setNome("Pedro");
        cliente5.setEmail("pedro@gmail.com");
        cliente5.setSobrenome("Oliveira");
        cliente5.setSexo("Masculino");
        cliente5.setDataNascimento(new Date(1995, 7, 12));
        cliente5.setNacionalidade("Brasileira");
        cliente5.setEndereco("Rua E, 456");
        cliente5.setCidade("Belo Horizonte");
        cliente5.setEstado("MG");
        cliente5.setTelefone("(31) 9876-5432");
        clienteRepository.save(cliente5);

        Cliente cliente6 = new Cliente();
        cliente6.setNome("Luciana");
        cliente6.setEmail("luciana@gmail.com");
        cliente6.setSobrenome("Mendes");
        cliente6.setSexo("Feminino");
        cliente6.setDataNascimento(new Date(1989, 9, 5));
        cliente6.setNacionalidade("Brasileira");
        cliente6.setEndereco("Rua F, 789");
        cliente6.setCidade("Porto Alegre");
        cliente6.setEstado("RS");
        cliente6.setTelefone("(51) 9876-5432");
        clienteRepository.save(cliente6);

        Cliente cliente7 = new Cliente();
        cliente7.setNome("Bruno");
        cliente7.setEmail("bruno@gmail.com");
        cliente7.setSobrenome("Pereira");
        cliente7.setSexo("Masculino");
        cliente7.setDataNascimento(new Date(1991, 11, 23));
        cliente7.setNacionalidade("Brasileira");
        cliente7.setEndereco("Rua G, 123");
        cliente7.setCidade("Recife");
        cliente7.setEstado("PE");
        cliente7.setTelefone("(81) 9876-5432");
        clienteRepository.save(cliente7);

        Cliente cliente8 = new Cliente();
        cliente8.setNome("Joãossss");
        cliente8.setEmail("joaosss@gmail.com");
        cliente8.setSobrenome("Santosss");
        cliente8.setSexo("Masculino");
        cliente8.setDataNascimento(new Date(1990, 5, 15));
        cliente8.setNacionalidade("Brasileira");
        cliente8.setEndereco("Rua C, 789s");
        cliente8.setCidade("São Paulo");
        cliente8.setEstado("SP");
        cliente8.setTelefone("(11) 9876-5432");
        clienteRepository.save(cliente8);

        Cliente cliente9 = new Cliente();
        cliente9.setNome("Anaaa");
        cliente9.setEmail("anaaa@gmail.com");
        cliente9.setSobrenome("Silvaaa");
        cliente9.setSexo("Feminino");
        cliente9.setDataNascimento(new Date(1987, 2, 10));
        cliente9.setNacionalidade("Brasileira");
        cliente9.setEndereco("Rua D, 123");
        cliente9.setCidade("Curitiba");
        cliente9.setEstado("PR");
        cliente9.setTelefone("(41) 9876-5432");
        clienteRepository.save(cliente9);

        Cliente cliente10 = new Cliente();
        cliente10.setNome("Pedross");
        cliente10.setEmail("pedrosss@gmail.com");
        cliente10.setSobrenome("Oliveirasss");
        cliente10.setSexo("Masculino");
        cliente10.setDataNascimento(new Date(1995, 7, 12));
        cliente10.setNacionalidade("Brasileira");
        cliente10.setEndereco("Rua E, 456");
        cliente10.setCidade("Belo Horizonte");
        cliente10.setEstado("MG");
        cliente10.setTelefone("(31) 9876-5432");
        clienteRepository.save(cliente10);

        Cliente cliente11 = new Cliente();
        cliente11.setNome("Lucianaaa");
        cliente11.setEmail("lucianaaa@gmail.com");
        cliente11.setSobrenome("Mendesaa");
        cliente11.setSexo("Feminino");
        cliente11.setDataNascimento(new Date(1989, 9, 5));
        cliente11.setNacionalidade("Brasileira");
        cliente11.setEndereco("Rua F, 789");
        cliente11.setCidade("Porto Alegre");
        cliente11.setEstado("RS");
        cliente11.setTelefone("(51) 9876-5432");
        clienteRepository.save(cliente11);

        Cliente cliente12 = new Cliente();
        cliente12.setNome("Bruno");
        cliente12.setEmail("brunosss@gmail.com");
        cliente12.setSobrenome("Pereira");
        cliente12.setSexo("Masculino");
        cliente12.setDataNascimento(new Date(1991, 11, 23));
        cliente12.setNacionalidade("Brasileira");
        cliente12.setEndereco("Rua G, 123");
        cliente12.setCidade("Recife");
        cliente12.setEstado("PE");
        cliente12.setTelefone("(81) 9876-5432");
        clienteRepository.save(cliente12);

        Pedido pedido1 = new Pedido();
        pedido1.setCliente(cliente1);
        pedido1.setDataPedido(new Date());

        List<BigDecimal> valorPedidos1 = new ArrayList<>();
        produtosPedido1.forEach(produto -> valorPedidos1.add(produto.getValorVenda()
                .multiply(new BigDecimal(produto.getQuantidade()))));
        BigDecimal valorPedido1 = valorPedidos1.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido1.setValorTotal(valorPedido1);
        pedido1.setProdutos(produtosPedido1);
        pedido1.setAtendente(usuario4);
        pedidoRepository.save(pedido1);

        Pedido pedido2 = new Pedido();
        pedido2.setCliente(cliente1);
        pedido2.setDataPedido(new Date());

        List<BigDecimal> valorPedidos2 = new ArrayList<>();
        produtosPedido2.forEach(produto -> valorPedidos2.add(produto.getValorVenda()
                .multiply(new BigDecimal(produto.getQuantidade()))));
        BigDecimal valorPedido2 = valorPedidos2.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido2.setValorTotal(valorPedido2);
        //pedido2.setValorTotal(new BigDecimal(200.00));
        pedido2.setProdutos(produtosPedido2);
        pedido2.setAtendente(usuario1);
        pedidoRepository.save(pedido2);

        Pedido pedido3 = new Pedido();
        pedido3.setCliente(cliente2);
        pedido3.setDataPedido(new Date());

        List<BigDecimal> valorPedidos3 = new ArrayList<>();
        produtosPedido3.forEach(produto -> valorPedidos3.add(produto.getValorVenda()
                .multiply(new BigDecimal(produto.getQuantidade()))));
        BigDecimal valorPedido3 = valorPedidos3.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido3.setValorTotal(valorPedido3);
        //pedido3.setValorTotal(new BigDecimal(150.00));
        pedido3.setProdutos(produtosPedido3);
        pedido3.setAtendente(usuario4);
        pedidoRepository.save(pedido3);

        Pedido pedido4 = new Pedido();
        pedido4.setCliente(cliente2);
        pedido4.setDataPedido(new Date());
        //pedido4.setValorTotal(new BigDecimal(300.00));
        pedido4.setAtendente(usuario2);
        pedidoRepository.save(pedido4);

        Pedido pedido1x = new Pedido();
        pedido1x.setCliente(cliente1);
        pedido1x.setDataPedido(new Date());
        //pedido1x.setValorTotal(new BigDecimal(100.00));
        pedido1x.setAtendente(usuario4);
        pedidoRepository.save(pedido1x);

        Pedido pedido2x = new Pedido();
        pedido2x.setCliente(cliente2);
        pedido2x.setDataPedido(new Date());
        //pedido2x.setValorTotal(new BigDecimal(200.00));
        pedido2x.setAtendente(usuario4);
        pedidoRepository.save(pedido2x);

        Pedido pedido3x = new Pedido();
        pedido3x.setCliente(cliente3);
        pedido3x.setDataPedido(new Date());
        //pedido3x.setValorTotal(new BigDecimal(300.00));
        pedido3x.setAtendente(usuario1);
        pedidoRepository.save(pedido3x);

        Pedido pedido4x = new Pedido();
        pedido4x.setCliente(cliente4);
        pedido4x.setDataPedido(new Date());
        //pedido4x.setValorTotal(new BigDecimal(400.00));
        pedido4x.setAtendente(usuario4);
        pedidoRepository.save(pedido4x);

        Pedido pedido5 = new Pedido();
        pedido5.setCliente(cliente5);
        pedido5.setDataPedido(new Date());
        //pedido5.setValorTotal(new BigDecimal(500.00));
        pedido5.setAtendente(usuario3);
        pedidoRepository.save(pedido5);

        Pedido pedido6 = new Pedido();
        pedido6.setCliente(cliente6);
        pedido6.setDataPedido(new Date());
        //pedido6.setValorTotal(new BigDecimal(600.00));
        pedido6.setAtendente(usuario3);
        pedidoRepository.save(pedido6);

        Pedido pedido7 = new Pedido();
        pedido7.setCliente(cliente7);
        pedido7.setDataPedido(new Date());
        //pedido7.setValorTotal(new BigDecimal(700.00));
        pedido7.setAtendente(usuario3);
        pedidoRepository.save(pedido7);

        Pedido pedido8 = new Pedido();
        pedido8.setCliente(cliente8);
        pedido8.setDataPedido(new Date());
        //pedido8.setValorTotal(new BigDecimal(800.00));
        pedido8.setAtendente(usuario3);
        pedidoRepository.save(pedido8);

        Pedido pedido9 = new Pedido();
        pedido9.setCliente(cliente9);
        pedido9.setDataPedido(new Date());
        //pedido9.setValorTotal(new BigDecimal(900.00));
        pedido9.setAtendente(usuario1);
        pedidoRepository.save(pedido9);

        Pedido pedido10 = new Pedido();
        pedido10.setCliente(cliente10);
        pedido10.setDataPedido(new Date());
        //pedido10.setValorTotal(new BigDecimal(1000.00));
        pedido10.setAtendente(usuario3);
        pedidoRepository.save(pedido10);

    }

}
