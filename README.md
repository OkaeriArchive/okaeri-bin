# Okaeri Binary Data Format (okaeri-bin)

![License](https://img.shields.io/github/license/OkaeriPoland/okaeri-bin)
![Total lines](https://img.shields.io/tokei/lines/github/OkaeriPoland/okaeri-bin)
![Repo size](https://img.shields.io/github/repo-size/OkaeriPoland/okaeri-bin)
![Contributors](https://img.shields.io/github/contributors/OkaeriPoland/okaeri-bin)
[![Discord](https://img.shields.io/discord/589089838200913930)](https://discord.gg/hASN5eX)

Simple binary-like format with built-in data deduplication. The library size is ~17kB.

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
  <version>2.0.1</version>
</dependency>
```

### Gradle

Add repository to the `repositories` section:

```groovy
maven { url "https://storehouse.okaeri.eu/repository/maven-public/" }
```

Add dependency to the `maven` section:

```groovy
implementation 'eu.okaeri:okaeri-bin:2.0.1'
```

## Code example

```java
// create bin
Bin bin = new Bin();
// insert data - strings
bin.put("hello", "welcome everyone");
bin.put("hiii", String.valueOf(12312));
bin.put("xxxxxxxx", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("renerene", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("nananana", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("hackin", "\0\0hallo");
bin.put("multiline-string", "line1\nline2\\nline2 too\nline3");
// inser data - collections
bin.put("list-of-values", Arrays.asList(String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4), String.valueOf(5)));
// insert data - maps
Map<String, String> map = new LinkedHashMap<>();
map.put("hackin", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
map.put("hackin2", "kop234kop 423kok 4o32 ko4pko4 p3opk4 2");
bin.put("map", map);
bin.put("submap", Collections.singletonMap("hmm", map));
// save to string
bin.write();
// load from string
bin.load("");
```

## Format example

```text
0 hello
1 welcome everyone
2 hiii
3 12312
4 xxxxxxxx
5 kop234kop 423kok 4o32 ko4pko4 p3opk4 2
6 nenenene
7 nananana
8 hackin
9   hallo
10 multiline-string
11 line1\nline2\\nline2 too\nline3
12 list-of-values
13 1
14 2
15 3
16 4
17 5
18 13 14 15 16 17
19 map
20 hackin2
21 85 205
22 submap
23 hmm
24 2321
25 01 23 45 65 75 89 1011 1218 1921 2224
```

## Supported types

To preserve small size okaeri-bin does not support any other types than String, Collection or Map.

- `T`: base type `String` or any other type specified in this section
- `Collection<T>`: list, set or other collection, returned as ArrayList
- `Map<T, T>`: maps, stored ordered and returned as LinkedHashMap

## Storing as file

It is highly recommended to use `.obdf` extension when saving okaeri-bin files to the disk.

## Limitations

Current implementation allows a maximum of `9,223,372,036,854,775,807` unique elements to be stored, including child elements and any other nested elements.
