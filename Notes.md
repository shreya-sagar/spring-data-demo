If you are looking to load your full application configuration, but use an embedded database, you should consider `@SpringBootTest` combined with `@AutoConfigureTestDatabase` rather than `@DataJpaTest`.

------------------------------------------------------
`@Deprecated
isEqualToComparingFieldByField( Object other )`
Use the recursive comparison by calling `usingRecursiveComparison()`.
This method is deprecated because it only compares the first level of fields while the recursive comparison traverses all fields recursively (only stopping at java types).
For example suppose actual and expected are of type A which has the following structure:
A
|— B b
|    |— String s
|    |— C c
|         |— String s
|         |— Date d
|— int i
isEqualToComparingFieldByField will compare actual and expected A.b and A.i fields but not B fields (it calls B equals method instead comparing B fields). The recursive comparison on the other hand will introspect B fields and then C fields and will compare actual and expected respective fields values, that is: A.i, A.B.s, A.B.C.s and A.B.C.d.
Concretely instead of writing:
`assertThat(actual).isEqualToComparingFieldByField(expected);`
You should write:
`assertThat(actual).usingRecursiveComparison().isEqualTo(expected);`