package by.nortin.restjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import by.nortin.restjwt.domain.BaseEntity;
import by.nortin.restjwt.dto.BaseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaseMapperTest {

    @Autowired
    private BaseMapper baseMapper;

    @Test
    void test_convertToDto() {
        Long id = 1L;
        BaseDto baseDto = new BaseDto();
        baseDto.setId(id);
        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId(id);

        assertEquals(baseDto, baseMapper.convertToDto(baseEntity));
    }
}