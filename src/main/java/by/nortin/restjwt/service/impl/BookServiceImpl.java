package by.nortin.restjwt.service.impl;

import by.nortin.restjwt.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl {

    private final BookMapper bookMapper;
}
