    package com.comunired.usuarios.graphql.resolver;


    import java.util.Collections;
    import java.util.List;
    import java.util.stream.Collectors;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageImpl;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.graphql.data.method.annotation.Argument;
    import org.springframework.graphql.data.method.annotation.MutationMapping;
    import org.springframework.graphql.data.method.annotation.QueryMapping;
    import org.springframework.stereotype.Controller;

    import com.comunired.usuarios.application.dto.AuthPayload;
    import com.comunired.usuarios.application.dto.UsuariosDTO;
    import com.comunired.usuarios.application.service.AuthService;
    import com.comunired.usuarios.application.service.UsuariosService;
    import com.comunired.usuarios.application.service.PasswordResetService;
    import com.comunired.usuarios.domain.entity.Usuario;


    import org.springframework.beans.factory.annotation.Autowired;
    import java.time.LocalDateTime;
    import java.time.LocalDate;
    import java.time.Instant;

    import java.util.Map;
    import java.util.HashMap;
    import java.util.Collections;

    import org.springframework.web.multipart.MultipartFile;
    import com.comunired.usuarios.infrastructure.cloudinary.CloudinaryService;
    import org.springframework.beans.factory.annotation.Autowired;

    import java.time.Duration;

    @Controller
    public class UsuariosResolver {

        private static final Logger logger = LoggerFactory.getLogger(UsuariosResolver.class);

        private final UsuariosService usuariosService;
        private final AuthService authService;
        private final PasswordResetService passwordResetService;

        @Autowired
        private CloudinaryService cloudinaryService;

        public UsuariosResolver(UsuariosService usuariosService, AuthService authService, PasswordResetService passwordResetService) {
            this.usuariosService = usuariosService;
            this.authService = authService;
            this.passwordResetService = passwordResetService;
        }

        private UsuariosDTO toDTO(Usuario usuario) {
            if (usuario == null) return null;

            boolean missing = false;
            StringBuilder missingFields = new StringBuilder();

            String id = usuario.getId();
            
            String foto_perfil = usuario.getFoto_perfil();
            if (foto_perfil == null) {
                foto_perfil = "";
            }
            
            String nombre = usuario.getNombre();
            if (nombre == null) { 
                missing = true; 
                missingFields.append("nombre "); 
                nombre = ""; 
            }
            
            String apellido = usuario.getApellido();
            if (apellido == null) { 
                missing = true; 
                missingFields.append("apellido "); 
                apellido = ""; 
            }
            
            String dni = usuario.getDni();
            if (dni == null) { 
                missing = true; 
                missingFields.append("dni "); 
                dni = ""; 
            }
            
            String numero_telefono = usuario.getNumero_telefono();
            if (numero_telefono == null) {
                numero_telefono = "";
            }
            
            String sexo = usuario.getSexo();
            if (sexo == null) {
                sexo = "";
            }
            
            String distrito = usuario.getDistrito();
            if (distrito == null) {
                distrito = "";
            }
            
            String codigo_postal = usuario.getCodigo_postal();
            if (codigo_postal == null) {
                codigo_postal = "";
            }
            
            String direccion = usuario.getDireccion();
            if (direccion == null) {
                direccion = "";
            }
            
            String email = usuario.getEmail();
            if (email == null) { 
                missing = true; 
                missingFields.append("email "); 
                email = ""; 
            }
            
            String rol_id = usuario.getRol_id();
            if (rol_id == null) {
                rol_id = "";
            }

            LocalDate fecha_nacimiento = usuario.getFecha_nacimiento();
            Instant fecha_registro = usuario.getFecha_registro();

            if (missing) {
                logger.warn("Usuario id={} tiene campos nulos: {} — se han normalizado temporalmente para cumplir schema GraphQL",
                    id, missingFields.toString().trim());
            }

            UsuariosDTO dto = new UsuariosDTO();
            dto.setId(id != null ? id : "");
            dto.setFoto_perfil(foto_perfil);
            dto.setNombre(nombre);
            dto.setApellido(apellido);
            dto.setDni(dni);
            dto.setNumero_telefono(numero_telefono);

            dto.setSexo(sexo);
            dto.setDistrito(distrito);
            dto.setCodigo_postal(codigo_postal);
            dto.setDireccion(direccion);
            dto.setEmail(email);
            dto.setRol_id(rol_id);

            dto.setFecha_nacimiento(fecha_nacimiento);
            dto.setFecha_registro(fecha_registro);
            dto.setUltimaActividad(usuario.getUltimaActividad());

            return dto;
        }

        /**
         * Query paginada de usuarios. En caso de excepción devolvemos Page vacía (para
         * evitar que GraphQL devuelva errors[] que anulan data).
         */
        @QueryMapping
        public Page<UsuariosDTO> obtenerUsuarios(@Argument int page, @Argument int size) {
            try {
                Page<Usuario> usuarios = usuariosService.obtenerUsuarios(page, size);
                if (usuarios == null) {
                    logger.warn("UsuariosService.obtenerUsuarios devolvió null (page={}, size={}), devolviendo Page vacía.", page, size);
                    return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
                }
                List<UsuariosDTO> dtos = usuarios.getContent().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
                return new PageImpl<>(dtos, usuarios.getPageable(), usuarios.getTotalElements());
            } catch (Exception e) {
                logger.error("Error en obtenerUsuarios(page={}, size={}). Devolviendo Page vacía para evitar error GraphQL.", page, size, e);
                return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);
            }
        }

        @QueryMapping
        public List<UsuariosDTO> obtenerTodosLosUsuarios() {
            try {
                List<Usuario> usuarios = usuariosService.obtenerTodosLosUsuarios();
                logger.info("Usuarios totales obtenidos: {}", usuarios.size());

                if (usuarios == null) {
                    logger.warn("UsuariosService.obtenerTodosLosUsuarios devolvió null, devolviendo lista vacía.");
                    return Collections.emptyList();
                }
                return usuarios.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            } catch (Exception e) {
                logger.error("Error en obtenerTodosLosUsuarios(), devolviendo lista vacía.", e);
                return Collections.emptyList();
            }
        }

        @QueryMapping
        public long contarUsuariosPorRol(@Argument String rol_id) {
            try {
                return usuariosService.contarPorRol(rol_id);
            } catch (Exception e) {
                logger.error("Error en contarUsuariosPorRol(rol_id={})", rol_id, e);
                return 0L;
            }
        }

        @QueryMapping
        public UsuariosDTO obtenerUsuarioPorId(@Argument String id) {
            try {
                Usuario u = usuariosService.buscarPorId(id);
                if (u == null) return null;
                return toDTO(u);
            } catch (Exception e) {
                logger.error("Error en obtenerUsuarioPorId(id={})", id, e);
                return null;
            }
        }

        // UsuariosResolver.java - mejora el método crearUsuario
        @MutationMapping
        public UsuariosDTO crearUsuario(@Argument Usuario usuario) {
            // Log inicial: datos recibidos
            logger.info("[crearUsuario] Payload recibido: email={}, nombre={}, fecha_nacimiento={}, fecha_registro={}",
                usuario.getEmail(), usuario.getNombre(), usuario.getFecha_nacimiento(), usuario.getFecha_registro());

            try {
                // Llama al service para guardar el usuario
                Usuario usuarioGuardado = usuariosService.guardarUsuario(usuario);

                // Si no se guardó correctamente
                if (usuarioGuardado == null) {
                    logger.error("[crearUsuario] Service retornó null para email={}", usuario.getEmail());
                    return null;
                }

                // Convierte el entity a DTO y loguea el resultado
                UsuariosDTO dto = toDTO(usuarioGuardado);
                logger.info("[crearUsuario] Usuario creado: id={}, email={}, fecha_nac={}, fecha_reg={}", 
                    dto.getId(), dto.getEmail(), dto.getFecha_nacimiento(), dto.getFecha_registro());
                return dto;

            } catch (Exception e) {
                logger.error("[crearUsuario] Error creando usuario: email={}", usuario != null ? usuario.getEmail() : "null", e);
                return null;
            }
        }


        @MutationMapping
        public UsuariosDTO actualizarUsuario(@Argument String id, @Argument Usuario usuario) {
            try {
                Usuario existente = usuariosService.buscarPorId(id);
                if (existente != null) {
                    usuario.setId(id);

                    // Preservar password si no se envía
                    if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                        usuario.setPassword(existente.getPassword());
                    }

                    // Preservar rol_id si no se envía
                    if (usuario.getRol_id() == null || usuario.getRol_id().isEmpty()) {
                        usuario.setRol_id(existente.getRol_id());
                    }

                    // ✅ IMPORTANTE: Preservar fecha_registro (nunca debe cambiar)
                    usuario.setFecha_registro(existente.getFecha_registro());

                    // Log de la fecha de nacimiento actualizada
                    logger.info("📅 Actualizando usuario: fecha_nac={}, fecha_reg={}", 
                        usuario.getFecha_nacimiento(), usuario.getFecha_registro());

                    Usuario actualizado = usuariosService.guardarUsuario(usuario);
                    return toDTO(actualizado);
                }
                
                logger.warn("⚠️ Usuario no encontrado con id: {}", id);
                return null;
                
            } catch (Exception e) {
                logger.error("❌ Error actualizando usuario id={}", id, e);
                return null;
            }
        }

        @MutationMapping
        public Boolean eliminarUsuario(@Argument String id) {
            try {
                usuariosService.eliminarUsuario(id);
                return true;
            } catch (Exception e) {
                logger.error("Error eliminando usuario id={}", id, e);
                return false;
            }
        }

        @MutationMapping
        public AuthPayload login(@Argument String email, @Argument String password) {
            try {
                return authService.login(email, password);
            } catch (Exception e) {
                logger.error("Error en login", e);
                return null;
            }
        }



        @MutationMapping
        public Boolean solicitarCodigoRecuperacion(@Argument String email) {
            try {
                return passwordResetService.solicitarCodigoRecuperacion(email);
            } catch (Exception e) {
                logger.error("Error en solicitarCodigoRecuperacion para email: {}", email, e);
                return false;
            }
        }

        @MutationMapping
        public Boolean verificarCodigoRecuperacion(@Argument String email, @Argument String codigo) {
            try {
                return passwordResetService.verificarCodigo(email, codigo);
            } catch (Exception e) {
                logger.error("Error en verificarCodigoRecuperacion para email: {}", email, e);
                return false;
            }
        }

        @MutationMapping
        public Boolean cambiarPasswordConCodigo(
                @Argument String email, 
                @Argument String codigo, 
                @Argument String nuevaPassword) {
            try {
                return passwordResetService.cambiarPasswordConCodigo(email, codigo, nuevaPassword);
            } catch (Exception e) {
                logger.error("Error en cambiarPasswordConCodigo para email: {}", email, e);
                return false;
            }
        }


        @QueryMapping
        public Map<String, Object> buscarUsuarios(@Argument String termino,
                                                @Argument int page,
                                                @Argument int size) {
            Page<UsuariosDTO> resultado = usuariosService.buscarUsuarios(termino, page, size);
            return createPageResponse(resultado);
        }

        @QueryMapping
        public Map<String, Object> buscarUsuariosPorNombre(@Argument String nombre,
                                                            @Argument int page,
                                                            @Argument int size) {
            Page<UsuariosDTO> resultado = usuariosService.buscarPorNombre(nombre, page, size);
            return createPageResponse(resultado);
        }

        @QueryMapping
        public Map<String, Object> usuariosNoSeguidos(@Argument String usuarioId,
                                                    @Argument int page,
                                                    @Argument int size) {
            Page<UsuariosDTO> resultado = usuariosService.obtenerUsuariosExcluyendo(
                    Collections.singletonList(usuarioId), page, size);
            return createPageResponse(resultado);
        }

        @QueryMapping
        public Map<String, Object> usuariosSugeridos(@Argument String usuarioId,
                                                    @Argument int page,
                                                    @Argument int size) {
            return usuariosNoSeguidos(usuarioId, page, size);
        }

        private Map<String, Object> createPageResponse(Page<UsuariosDTO> page) {
            Map<String, Object> response = new HashMap<>();
            response.put("content", page.getContent());
            response.put("totalElements", page.getTotalElements());
            response.put("totalPages", page.getTotalPages());
            response.put("number", page.getNumber());
            response.put("size", page.getSize());
            return response;
        }



        @MutationMapping
        public String subirFotoPerfil(@Argument String usuarioId, 
                                    @Argument MultipartFile archivo) {
            try {
                logger.info("📸 Subiendo foto de perfil para usuario: {}", usuarioId);
                
                // Buscar usuario
                Usuario usuario = usuariosService.buscarPorId(usuarioId);
                if (usuario == null) {
                    logger.error("❌ Usuario no encontrado: {}", usuarioId);
                    throw new IllegalArgumentException("Usuario no encontrado");
                }
                
                // Eliminar foto anterior si existe
                String fotoAnterior = usuario.getFoto_perfil();
                if (fotoAnterior != null && fotoAnterior.contains("cloudinary.com")) {
                    logger.info("🗑️ Eliminando foto anterior...");
                    cloudinaryService.eliminarImagen(fotoAnterior);  // ← Llamada al service
                }
                
                // Subir nueva imagen a Cloudinary
                String nuevaUrl = cloudinaryService.subirImagen(archivo);  // ← Llamada al service
                
                // Actualizar usuario con nueva URL
                usuario.setFoto_perfil(nuevaUrl);
                usuariosService.guardarUsuario(usuario);
                
                logger.info("✅ Foto de perfil actualizada: {}", nuevaUrl);
                return nuevaUrl;
                
            } catch (Exception e) {
                logger.error("❌ Error subiendo foto de perfil: {}", e.getMessage(), e);
                throw new RuntimeException("Error subiendo imagen: " + e.getMessage());
            }
        }

        @MutationMapping
        public Boolean eliminarFotoPerfil(@Argument String usuarioId) {
            try {
                logger.info("🗑️ Eliminando foto de perfil de usuario: {}", usuarioId);
                
                // Buscar usuario
                Usuario usuario = usuariosService.buscarPorId(usuarioId);
                if (usuario == null) {
                    logger.error("❌ Usuario no encontrado: {}", usuarioId);
                    return false;
                }
                
                String fotoActual = usuario.getFoto_perfil();
                if (fotoActual == null || fotoActual.isEmpty()) {
                    logger.warn("⚠️ Usuario no tiene foto de perfil");
                    return false;
                }
                
                // Eliminar de Cloudinary si es URL de Cloudinary
                if (fotoActual.contains("cloudinary.com")) {
                    boolean eliminado = cloudinaryService.eliminarImagen(fotoActual);  // ← Llamada al service
                    if (!eliminado) {
                        logger.warn("⚠️ No se pudo eliminar de Cloudinary, continuando...");
                    }
                }
                
                // Limpiar campo en usuario
                usuario.setFoto_perfil(null);
                usuariosService.guardarUsuario(usuario);
                
                logger.info("✅ Foto de perfil eliminada");
                return true;
                
            } catch (Exception e) {
                logger.error("❌ Error eliminando foto de perfil: {}", e.getMessage(), e);
                return false;
            }
        }

        @MutationMapping
        public String inicializarActividades() {
            try {
                // ✅ Usar el service en lugar del repository
                List<Usuario> usuarios = usuariosService.obtenerTodosLosUsuarios();
                int actualizados = 0;
                Instant ahora = Instant.now();
                
                for (Usuario usuario : usuarios) {
                    if (usuario.getUltimaActividad() == null) {
                        usuario.setUltimaActividad(ahora);
                        usuariosService.guardarUsuario(usuario); // ✅ Usar el service
                        actualizados++;
                    }
                }
                
                return "✅ Actualizados " + actualizados + " usuarios con ultimaActividad";
                
            } catch (Exception e) {
                logger.error("Error inicializando actividades", e);
                return "❌ Error: " + e.getMessage();
            }
        }
    }
