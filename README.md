# Okaeri Binary Data Format (okaeri-bin)

![License](https://img.shields.io/github/license/OkaeriPoland/okaeri-bin)
![Total lines](https://img.shields.io/tokei/lines/github/OkaeriPoland/okaeri-bin)
![Repo size](https://img.shields.io/github/repo-size/OkaeriPoland/okaeri-bin)
![Contributors](https://img.shields.io/github/contributors/OkaeriPoland/okaeri-bin)
[![Discord](https://img.shields.io/discord/589089838200913930)](https://discord.gg/hASN5eX)

Simple binary format with built-in data deduplication. The library size is ~15kB.

## Installation
### Maven
Add repository to the `repositories` section:
```xml
<repository>
    <id>okaeri-repo</id>
    <url>https://storehouse.okaeri.eu/repository/maven-public/</url>
</repository>
```
Add dependency to the `dependencies` section:
```xml
<dependency>
  <groupId>eu.okaeri</groupId>
  <artifactId>okaeri-bin</artifactId>
  <version>1.0.0</version>
</dependency>
```
### Gradle
Add repository to the `repositories` section:
```groovy
maven { url "https://storehouse.okaeri.eu/repository/maven-public/" }
```
Add dependency to the `maven` section:
```groovy
implementation 'eu.okaeri:okaeri-bin:1.0.0'
```

## Code example
```java
// create bin
Bin bin = new Bin();
// insert data - strings
bin.put("hello", "welcome everyone");
bin.put("hiii", String.valueOf(12312));
bin.put("xxxxxxxx", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("nenenene", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("nananana", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("hackin", "\0\0hallo");
// inser data - collections
bin.put("list-of-values", Arrays.asList(String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4), String.valueOf(5)));
// insert data - maps
Map<String, String> map = new LinkedHashMap<>();
map.put("hackin", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
map.put("hackin2", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("submap", map);
// save to string
bin.write();
// load from string
bin.load("");
```

## Format example
```text
0   hello
1   welcome everyone
2   hiii
3   12312
4   list-of-values
5   1
6   2
7   3
8   4
9   5
10  5 6 7 8 9
11   xxxxxxxx
12   kop234kop 423kok 4o32 ko4pko4 p3opk4 2
13   nenenene
14   nananana
15   hackin
16     hallo
17   submap
18   hackin2
19  1512 1812
20  01 23 410 1112 1312 1412 1516 1719
```

## Supported types
- `String`: base type, to preserve small size okaeri-bin does not support other types
- `Collection<String>`: allows to store lists, sets, and other collections
- `Map<String, String>`: stores maps and always preserves the order using LinkedHashMap

## Storing as file
It is highly recommended to use `.obdf` extension when saving okaeri-bin files to the disk.
