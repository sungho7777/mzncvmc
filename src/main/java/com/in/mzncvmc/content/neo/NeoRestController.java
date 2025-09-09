package com.in.mzncvmc.content.neo;

import com.in.mzncvmc.content.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/neo")
public class NeoRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final NeoService neoService;

    @Autowired
    public NeoRestController(NeoService neoService) {
        this.neoService = neoService;
    }




    // Read All
    @GetMapping
    public List<Neo> getList() {
        logger.debug("NeoController.getList");


        return neoService.findAll();
    }
    // Read One
    @GetMapping("/{id}")
    public Neo getData(@PathVariable Long id) {
        logger.debug("NeoController.getData : " + id);


        return neoService.findById(id);
    }

    // Create
    @PostMapping
    public ApiResponse<Neo> createData(@RequestBody Neo neo) {
        logger.debug("NeoController.createData : " + neo);

        neoService.insert(neo);


        return ApiResponse.success(neo, "Neo created successfully");
    }



    // Update
    @PutMapping("/{id}")
    public ApiResponse<Neo> updateData(@PathVariable Long id, @RequestBody Neo neo) {
        logger.debug("NeoController.updateData : " + neo);

        neo.setId(id);
        neoService.update(neo);


        return ApiResponse.success(neo, "Neo update successfully");
    }

    // Delete
    @DeleteMapping("/{id}")
    public ApiResponse<Long> deleteData(@PathVariable Long id) {
        logger.debug("NeoController.deleteData : " + id);

        neoService.delete(id);

        return ApiResponse.success(id, "Neo delete successfully"); // JSON으로 응답됨
    }
}
