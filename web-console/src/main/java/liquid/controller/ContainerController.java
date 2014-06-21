package liquid.controller;

import liquid.domain.Container;
import liquid.domain.FileInfo;
import liquid.facade.ContainerFacade;
import liquid.metadata.ContainerCap;
import liquid.metadata.ContainerStatus;
import liquid.metadata.ContainerType;
import liquid.metadata.LocationType;
import liquid.persistence.domain.ContainerEntity;
import liquid.persistence.domain.ContainerSubtypeEntity;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ContainerService;
import liquid.service.ContainerSubtypeService;
import liquid.service.LocationService;
import liquid.service.ServiceItemService;
import liquid.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 3:59 PM
 */
@Controller
@RequestMapping("/container")
public class ContainerController {
    private static final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    @Autowired
    private Environment env;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContainerFacade containerFacade;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceItemService serviceItemService;

    @ModelAttribute("container")
    public Container populateContainer() {
        return new Container();
    }

    @ModelAttribute("statusArray")
    public ContainerStatus[] populateStatus() {
        return ContainerStatus.values();
    }

    @ModelAttribute("containerTypeMap")
    public Map<Integer, String> populateContainerTypeMap() {
        return ContainerCap.toMap();
    }

    @ModelAttribute("containerTypes")
    public ContainerCap[] populateContainerTypes() {
        return ContainerCap.values();
    }

    @ModelAttribute("locations")
    public List<LocationEntity> populateLocations() {
        return locationService.findByType(LocationType.YARD.getType());
    }

    @ModelAttribute("containerSubtypes")
    public Iterable<ContainerSubtypeEntity> populateContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.SELF);
    }

    @ModelAttribute("owners")
    public Iterable<ServiceProviderEntity> populateOwners() {
        return serviceItemService.findContainerOwners();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model,
                       @RequestParam(required = false, defaultValue = "0") int number,
                       @RequestParam(required = false, defaultValue = "0") Long subtypeId,
                       @RequestParam(required = false, defaultValue = "0") Long ownerId,
                       @RequestParam(required = false, defaultValue = "0") Long yardId,
                       @RequestParam(required = false) String bicCode) {

        // container subtypes
        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);

        // Owner list
        List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();

        // Yard list
        List<LocationEntity> yards = locationService.findYards();

        if (null != bicCode && bicCode.trim().length() > 0) {
            List<ContainerEntity> containers = containerService.findBicCode(bicCode);
            if (null == containers) containers = Collections.EMPTY_LIST;
            final List<ContainerEntity> list = containers;
            model.addAttribute("page", new Page<ContainerEntity>() {
                @Override
                public int getTotalPages() { return 1; }

                @Override
                public long getTotalElements() { return list.size(); }

                @Override
                public boolean hasPreviousPage() { return false; }

                @Override
                public boolean isFirstPage() { return true; }

                @Override
                public boolean hasNextPage() { return false; }

                @Override
                public boolean isLastPage() { return true; }

                @Override
                public int getNumber() { return 0; }

                @Override
                public int getSize() { return list.size(); }

                @Override
                public int getNumberOfElements() { return 0;}

                @Override
                public List<ContainerEntity> getContent() {return list; }

                @Override
                public boolean hasContent() {return true; }

                @Override
                public Sort getSort() {return null; }

                @Override
                public boolean isFirst() {return true; }

                @Override
                public boolean isLast() { return false; }

                @Override
                public boolean hasNext() { return false; }

                @Override
                public boolean hasPrevious() { return false; }

                @Override
                public Pageable nextPageable() { return null; }

                @Override
                public Pageable previousPageable() { return null; }

                @Override
                public Iterator<ContainerEntity> iterator() { return null; }
            });
        } else {
            int size = 20;
            PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
            Page<ContainerEntity> page = containerService.findAll(subtypeId, ownerId, yardId, pageRequest);
            model.addAttribute("page", page);
        }

        model.addAttribute("subtypeId", subtypeId);
        model.addAttribute("subtypes", subtypes);

        model.addAttribute("ownerId", ownerId);
        model.addAttribute("owners", owners);

        model.addAttribute("yardId", yardId);
        model.addAttribute("yards", yards);
        return "container/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute Container container,
                      BindingResult bindingResult, Principal principal) {
        logger.debug("container: {}", container);

        if (bindingResult.hasErrors()) {
            return "container/list";
        } else {
            containerFacade.enter(container);
            return "redirect:/container";
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String initForm(Model model) {
        FileInfo[] fileInfos = new FileInfo[0];
        File uploadDir = new File(env.getProperty("upload.dir", "/opt/liquid/upload/"));
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            File[] files = uploadDir.listFiles();
            fileInfos = new FileInfo[files.length];
            for (int i = 0; i < fileInfos.length; i++) {
                fileInfos[i] = new FileInfo();
                fileInfos[i].setName(files[i].getName());
                fileInfos[i].setModifiedDate(DateUtils.stringOf(new Date(files[i].lastModified())));
            }
        }
        model.addAttribute("fileInfos", fileInfos);
        return "container/import";
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET, params = "filename")
    public String importFile(@RequestParam final String filename, Model model) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    containerService.importExcel(env.getProperty("upload.dir", "/opt/liquid/upload/") + filename);
                    logger.info("{} import is done.", filename);
                } catch (IOException e) {
                    logger.info("{} import failed.", filename);
                }
            }
        }).start();
        return "container/import";
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                try (FileOutputStream fos = new FileOutputStream(env.getProperty("upload.dir", "/opt/liquid/upload/") + file.getOriginalFilename())) {
                    fos.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/container/import";
    }
}
