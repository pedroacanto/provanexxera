package com.prova.nexxera.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/conta")
public class ContaController {
	
	@Autowired
	private ContaRepository repositoryConta;
	
	@Autowired
	private FilialRepository repositoryFilial;
		
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listar() {
		List<Conta> contas = repositoryConta.findAll(sortByLocalAsc());
		
		ModelAndView model = new ModelAndView("/conta/lista.html");
		model.addObject("contas", contas);
		return model;
		
	}
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public ModelAndView form(){
		List<Filial> filiais = repositoryFilial.findAll();
		
		ModelAndView model = new ModelAndView("/conta/form.html");
		model.addObject("filiais", filiais);
		
		return model;
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView salvar(@Valid Conta conta, BindingResult result, RedirectAttributes redirectAttributes) {
		try {			
			conta.setContaPaga(false);
			conta.setDataLancamento(LocalDate.now());
			
			//Thymeleaf não deixou passar direito o objeto filial pelo selected. Verificar possibilidade de ajuste.		
			Optional<Filial> filial = repositoryFilial.findById(conta.getFilialId());
			conta.setFilial(filial.get());
			
			repositoryConta.save(conta);
			redirectAttributes.addFlashAttribute("sucesso", "Conta cadastrada com sucesso!");
		}catch(Exception ex) {
			redirectAttributes.addFlashAttribute("erro", "Algo deu errado durante o cadastro!");
		}finally {
			return new ModelAndView("redirect:/conta");
		}
	}	
	
	@SuppressWarnings("finally")
	@RequestMapping(value="/baixa/{id}", method=RequestMethod.GET)
	public ModelAndView darBaixaConta(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {			
			Optional<Conta>  conta = repositoryConta.findById(id);
			Optional<Filial> filial = repositoryFilial.findById(conta.get().getFilial().getId());
			
			conta.get().setContaPaga(true);
			conta.get().setSaldoAnterior(filial.get().getSaldo());
			
			filial.get().setSaldo(filial.get().getSaldo().subtract(conta.get().getValor()));
			
			repositoryConta.save(conta.get());
			repositoryFilial.save(filial.get());
					
			redirectAttributes.addFlashAttribute("sucesso", "Baixa da conta realizada com sucesso!");
		}catch(Exception ex) {
			redirectAttributes.addFlashAttribute("erro", "Algo deu errado durante procedimento!");
		}finally {
			return new ModelAndView("redirect:/conta");
		}
	}
	
	@RequestMapping(value="/excluir/{id}", method=RequestMethod.GET)
	public ModelAndView deletar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		Optional<Conta> conta = repositoryConta.findById(id);
		repositoryConta.delete(conta.get());
		redirectAttributes.addFlashAttribute("sucesso", "Conta Excluida com sucesso!");
		return new ModelAndView("redirect:/conta");
	}
	
	@RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("id") Long id){
		Optional<Conta> conta = repositoryConta.findById(id);
		List<Filial> filiais = repositoryFilial.findAll();
		
		ModelAndView model = new ModelAndView("/conta/edit.html");
		
		model.addObject("contaEdit", conta.get());
		model.addObject("filiais", filiais);
		
		return model;
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public ModelAndView editar(@Valid Conta conta, BindingResult result, RedirectAttributes redirectAttributes) {
		try {			
			Optional<Filial> filial = repositoryFilial.findById(conta.getFilialId());			
			Optional<Conta> contaSalva = repositoryConta.findById(conta.getId());
			//BeanUtils.copyProperties(conta, contaSalva.get());
			
			contaSalva.get().setValor(conta.getValor());
			contaSalva.get().setFilial(filial.get());
			
			repositoryConta.save(contaSalva.get());		
			
			redirectAttributes.addFlashAttribute("sucesso", "Conta editada com sucesso!");
		}catch(Exception ex) {
			redirectAttributes.addFlashAttribute("erro", "Algo deu errado durante a edição!");
		}finally {
			return new ModelAndView("redirect:/conta");
		}
	}
	
	
	public Sort sortByLocalAsc() {
        return new Sort(Sort.Direction.ASC, "dataLancamento");
    }
}
