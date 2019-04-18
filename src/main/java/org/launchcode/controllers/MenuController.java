package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @RequestMapping(value="add", method=GET)
    public String add(Model model) {

        model.addAttribute(new Menu());
        model.addAttribute("title","Add Menu");

        return "menu/add";
    }

    @RequestMapping(value="add", method=POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {

        if(errors.hasErrors()) { return "menu/add"; }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value="view/{id}")
    public String viewMenu(@PathVariable("id") int id, Model model) {

        model.addAttribute("menu",menuDao.findOne(id));
        model.addAttribute("title",menuDao.findOne(id).getName());

        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}")
    public String addItem(@PathVariable("id") int menuId, Model model) {

        Menu menu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(menu,cheeseDao.findAll());

        model.addAttribute("form",form);
        model.addAttribute("title","Add item to menu: " + menu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value="add-item", method=POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {

        if(errors.hasErrors()) {
            return "menu/add-item";
        }

        Menu menu = menuDao.findOne(form.getMenuId());
        Cheese cheese = cheeseDao.findOne(form.getCheeseId());

        menu.addItem(cheese);

        menuDao.save(menu);

        return "redirect:view/" + form.getMenuId();
    }
}
