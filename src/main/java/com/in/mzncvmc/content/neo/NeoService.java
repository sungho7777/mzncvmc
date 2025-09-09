package com.in.mzncvmc.content.neo;

import com.in.mzncvmc.content.neo.mapper.NeoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeoService {
    private final NeoMapper neoMapper;

    public NeoService(NeoMapper neoMapper) {
        this.neoMapper = neoMapper;
    }
    public List<Neo> findAll() {
        return neoMapper.findAll();
    }
    public Neo findById(Long id) {
        return neoMapper.findById(id);
    }
    public void insert(Neo neo) {
        neoMapper.insert(neo);
    }
    public void update(Neo neo) {
        neoMapper.update(neo);
    }
    public void delete(Long id) {
        neoMapper.delete(id);
    }
}
