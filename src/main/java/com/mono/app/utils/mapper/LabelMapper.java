package com.mono.app.utils.mapper;

import com.mono.app.dto.response.LabelResponse;
import com.mono.app.model.Label;
import com.mono.app.utils.mapper.types.Mapper;
import org.springframework.stereotype.Component;

@Component("labelMapper")
public class LabelMapper implements Mapper<Label, LabelResponse> {
    @Override
    public LabelResponse toDTO(Label dao) {
        return new LabelResponse(
                dao.getName()
        );
    }
}
