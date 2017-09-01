use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;
use std::collections::BTreeMap;

fn sanitize_word(input: &str) -> String {
    let ret: String = input.chars().filter(|c| c.is_alphabetic()).collect();
    ret.to_lowercase()
}

fn main() {
    let file = File::open("data/BOM.txt").expect("Error opening file");
    let mut reader = BufReader::new(file);

    let mut frequency: BTreeMap<String, u32> = BTreeMap::new();

    for line in reader.lines() {
        if let Ok(line) = line {
            for word in line.split_whitespace() {
                *frequency.entry(sanitize_word(word)).or_insert(0) += 1;
            }
        } else {
            println!("Error reading line");
        }
    }
    
    println!("{:?}", frequency);
}
