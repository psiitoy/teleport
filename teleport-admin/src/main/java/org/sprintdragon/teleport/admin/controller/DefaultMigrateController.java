package org.sprintdragon.teleport.admin.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.sprintdragon.teleport.core.domain.IMigrateTask;
import org.sprintdragon.teleport.core.domain.MigrateTask;
import org.sprintdragon.teleport.core.domain.TaskStatItemEnum;
import org.sprintdragon.teleport.core.migrate.MigrateProcessor;
import org.sprintdragon.teleport.core.migrate.PersistentManager;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Date: 15-3-7
 * Time: 上午9:54
 */
@Controller
@RequestMapping({"/migrate"})
public class DefaultMigrateController {

    private Logger logger = LoggerFactory.getLogger(DefaultMigrateController.class);

    @Autowired
    private MigrateProcessor migrateProcessor;
    @Resource
    private PersistentManager persistentManager;

    @RequestMapping(value = {"", "/", "/view"})
    public String view(Model model, @RequestParam(required = false) String success, @RequestParam(required =
            false) String msg) {
        common_view(model, success, msg);
        return "migrate/view";
    }

    private void common_view(Model model, String success, String msg) {
        Map<String, String> typeMap = new TreeMap<String, String>();
        for (String key : persistentManager.getCheckMigrateMap().keySet()) {
            typeMap.put(key, key);
        }

        model.addAttribute("typeMap", typeMap);
        model.addAttribute("taskList", migrateProcessor.getAllDataTaskVo());
        model.addAttribute("success", success);
        model.addAttribute("msg", msg);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    @RequestMapping(value = {"/del"}, method = RequestMethod.GET)
    public String delData(Model model, @RequestParam Integer id) {
        migrateProcessor.remove(id);
        model.addAttribute("success", true);
        model.addAttribute("msg", "remove success");
        return "redirect:/migrate/view";
    }

    @RequestMapping(value = {"/task/{id}"}, method = RequestMethod.GET)
    public String viewTask(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("data", migrateProcessor.getTaskById(id));
        model.addAttribute("itemEnum", TaskStatItemEnum.values());
        return "migrate/result";
    }

    @RequestMapping(value = "/addMigrateTask", method = RequestMethod.POST)
    public String addMigrateTask(Model model, @Valid @ModelAttribute("query") DefaultMigrateQuery query, BindingResult
            bindingResult) {
        if (bindingResult.hasErrors()) {
            common_view(model, "", "");
            return "/migrate/view";
        }
        logger.info("addMigrateTask query={}", JSON.toJSONString(query));
        try {
            migrateProcessor.addTask(convertDataQueryVo(query));
            model.addAttribute("success", true);
            model.addAttribute("msg", "commit success");
        } catch (IOException e) {
            model.addAttribute("success", false);
            model.addAttribute("msg", "commit error, e:" + e.getMessage());
            logger.error("commit error", e);
        }
        return "redirect:/migrate/view";
    }

    public IMigrateTask convertDataQueryVo(final DefaultMigrateQuery query) throws IOException {
        final String[] strs = query.getType().split("->");
        query.setPageNo(1);
        query.setPageSize(query.getPageSize());
        MigrateTask task = new MigrateTask();
        task.setFromDbKey(strs[0]);
        task.setToDbKey(strs[1]);
        task.setQuery(query);
        return task;
    }

}
