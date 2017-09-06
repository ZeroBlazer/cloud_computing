use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;
// use std::collections::BTreeMap;
use std::collections::HashMap;

fn sanitize_word(input: &str) -> String {
    let ret: String = input.chars().filter(|c| c.is_alphabetic()).collect();
    ret.to_lowercase()
}

fn main() {
    //let file = File::open("data/file2.txt").expect("Error opening file");
     let file = File::open("/home/cs-unsax/Documents/ol_dump_works_2017-08-31.txt").expect("Error opening file");
    let reader = BufReader::new(file);

    // let mut frequency: BTreeMap<String, u32> = BTreeMap::new();
    let mut frequency: HashMap<String, u32> = HashMap::new();

    for line in reader.lines() {
        if let Ok(line) = line {
            for word in line.split_whitespace() {
                *frequency.entry(sanitize_word(word)).or_insert(0) += 1;
            }
        } else {
            println!("Error reading line");
        }
    }
    
    let mut ofile = File::create("Dictionary.txt").expect("Couldn't open write file");
    write!(ofile, "{:#?}\n", frequency).expect("Couldn't write in file");
    // println!("{:?}", frequency);
}
