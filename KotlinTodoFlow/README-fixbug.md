![bug Network CORS (Cross-Origin Resource Sharing) Error](imagesBug/bug-kotlinKMP.png)

## fix
before
private val baseUrl = "http://127.0.0.1:9000"

after
private val baseUrl = "http://10.0.2.2:9000" 

10.0.2.2 : localhost emulator
check ip device: Open terminal
```
ipconfig
```

