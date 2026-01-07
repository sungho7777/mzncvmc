package com.in.mzncvmc.content.neo;

import com.in.mzncvmc.common.system.response.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/neo")
public class NeoRestController {
    private final NeoService neoService;

    @Autowired
    public NeoRestController(NeoService neoService) {
        this.neoService = neoService;
    }




    // Read All
    @GetMapping
    public List<Neo> getList() {
        log.debug("NeoController.getList");


        return neoService.findAll();
    }
    // Read One
    @GetMapping("/{id}")
    public Neo getData(@PathVariable Long id) {
        log.debug("NeoController.getData : " + id);


        return neoService.findById(id);
    }

    // Create
    @PostMapping
    public ApiResponse<Neo> createData(@RequestBody Neo neo) {
        log.debug("NeoController.createData : " + neo);

        neoService.insert(neo);


        return ApiResponse.success(neo, "Neo created successfully");
    }



    // Update
    @PutMapping("/{id}")
    public ApiResponse<Neo> updateData(@PathVariable Long id, @RequestBody Neo neo) {
        log.debug("NeoController.updateData : " + neo);

        neo.setId(id);
        neoService.update(neo);


        return ApiResponse.success(neo, "Neo update successfully");
    }

    // Delete
    @DeleteMapping("/{id}")
    public ApiResponse<Long> deleteData(@PathVariable Long id) {
        log.debug("NeoController.deleteData : " + id);

        neoService.delete(id);

        return ApiResponse.success(id, "Neo delete successfully"); // JSON으로 응답됨
    }
}
