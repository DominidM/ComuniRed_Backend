package com.comunired.reels.application.port.in;

import com.comunired.reels.application.dto.in.CrearReelCommand;
import com.comunired.reels.application.dto.out.ReelResponse;

public interface CrearReelUseCase {
    ReelResponse ejecutar(CrearReelCommand command);
}
