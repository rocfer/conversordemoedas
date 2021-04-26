package com.example.conversor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    private String mensagem = "";

    @GetMapping("/")
    public String main(Model model) {
        Form form = new Form();
        model.addAttribute("form", form);
        model.addAttribute("mensagem", mensagem);
        return "main";
    }

    @PostMapping("converter")
    public String converter(Form form) {

        RestTemplate rest = new RestTemplate();

        ResponseEntity<Cambio[]> response = rest.getForEntity("https://economia.awesomeapi.com.br/USD-BRL/1", Cambio[].class);

        Cambio[] cambios = response.getBody();

        if (cambios.length == 0) {
            mensagem = "Não existe nenhuma cotação";
            return "redirect:/";
        }

        Cambio cambio = cambios[0];

        Double cotacao = Double.valueOf(cambio.getBid());

        String tipoConversao = form.getTipoConversao();
        Double valorAConverter = form.getValorAConverter();

        if (tipoConversao.equalsIgnoreCase("USD-BRL")) {
            Double valorConvertido = valorAConverter * cotacao;
            mensagem = formatar(valorAConverter) + " reais em dólares é " + formatar(valorConvertido);
        }
        else {
            Double valorConvertido = valorAConverter / cotacao;
            mensagem = formatar(valorAConverter) + " dólares em reais é " + formatar(valorConvertido);
        }

        return "redirect:/";
    }

    private String formatar(Double numero) {
        return String.format("%.2f", numero);
    }

}
