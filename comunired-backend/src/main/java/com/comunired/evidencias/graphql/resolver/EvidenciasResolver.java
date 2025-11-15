package com.comunired.evidencias.graphql.resolver;

import com.comunired.evidencias.application.service.EvidenciasService;
import com.comunired.evidencias.application.dto.EvidenciasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
public class EvidenciasResolver {

    @Autowired
    private EvidenciasService evidenciasService;

    // ========== QUERIES ==========
    
    @QueryMapping
    public List<EvidenciasDTO> evidenciasPorQueja(@Argument String quejaId) {
        return evidenciasService.findByQuejaId(quejaId);
    }

    // ========== MUTATIONS ==========
    
    @MutationMapping
    public EvidenciasDTO subirEvidencia(@Argument String quejaId, 
                                       @Argument MultipartFile archivo, 
                                       @Argument String tipo) {
        return evidenciasService.uploadEvidencia(quejaId, archivo, tipo);
    }

    @MutationMapping
    public Boolean eliminarEvidencia(@Argument String id) {
        return evidenciasService.delete(id);
    }
}
