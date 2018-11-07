package com.prova.nexxera.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prova.nexxera.api.model.Conta;
import com.prova.nexxera.api.model.Filial;
import com.prova.nexxera.api.repository.ContaRepository;
import com.prova.nexxera.api.repository.FilialRepository;

@Controller
@RequestMapping("/filial")
public class FilialController {
	
	@Autowired
	private FilialRepository repositoryFilial;
	
	@Autowired
	private ContaRepository repositoryConta;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listar() {
		List<Filial> filiais = repositoryFilial.findAll();
		
		ModelAndView model = new ModelAndView("/filial/lista.html");
		model.addObject("filiais", filiais);
		
		return model;		
	}
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public ModelAndView form(){		
		return new ModelAndView("/filial/form.html");		
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView salvar(@Valid Filial filial, BindingResult result, RedirectAttributes redirectAttributes) {
		try {			
			repositoryFilial.save(filial);
			redirectAttributes.addFlashAttribute("sucesso", "Conta cadastrada com sucesso!");
		}catch(Exception ex) {
			redirectAttributes.addFlashAttribute("erro", "Algo deu errado durante o cadastro!");
		}finally {
			return new ModelAndView("redirect:/filial");
		}
	}
	
	@RequestMapping(value="/visualizar/{id}", method = RequestMethod.GET)
	public ModelAndView visualizar(@PathVariable("id") Long id) {
		Optional<Filial> filial = repositoryFilial.findById(id);
		
		List<Conta> contas = repositoryConta.findContaByFilialId(id); 
		
		ModelAndView model = new ModelAndView("/filial/visualiza.html");
		
		model.addObject("filial", filial.get());	
		model.addObject("contas", contas);
		
		return model;
	}

}
