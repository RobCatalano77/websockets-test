const mainDiv = document.getElementById("mainDiv");

async function* makeTextFileLineIterator(fileURL) {
  const utf8Decoder = new TextDecoder("utf-8");
  let response = await fetch(fileURL);
  let reader = response.body.getReader();
  let {value: chunk, done: readerDone} = await reader.read();
  chunk = chunk ? utf8Decoder.decode(chunk, {stream: true}) : "";

  let re = /\r\n|\n|\r/gm;
  let startIndex = 0;

  for (;;) {
    let result = re.exec(chunk);
    if (!result) {
      if (readerDone) {
        break;
      }
      let remainder = chunk.substr(startIndex);
      ({value: chunk, done: readerDone} = await reader.read());
      chunk = remainder + (chunk ? utf8Decoder.decode(chunk, {stream: true}) : "");
      startIndex = re.lastIndex = 0;
      continue;
    }
    yield chunk.substring(startIndex, result.index);
    startIndex = re.lastIndex;
  }
  if (startIndex < chunk.length) {
    // last line didn't end in a newline char
    yield chunk.substr(startIndex);
  }
}
/*
let iter = makeTextFileLineIterator("./lines.txt");
let readString = iter.next();
while (!readString.done) {
  readString.then(response => console.log(response.value));
  readString = await iter.next();
}
*/

(async function() {
  let count = 1;
  for await (let num of makeTextFileLineIterator("http://localhost:4567/hello2")) {
    var table = document.getElementById("dataTable");
    var row = table.insertRow(count);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    cell1.innerHTML = count;
    cell2.innerHTML = num;
    count++;
  }
})();

/*
const gen = makeTextFileLineIterator("./lines.txt");
var next;
for (const xx of gen()) {
  console.log(xx);
}
*/
/*
for (let i = 0; i < numStrs; i++) {
  gen.next().then(response => console.log(response.value));
}
*/
/*for (const line of makeTextFileLineIterator("./lines.txt")) {
  console.log(line);
}
*/

/*
// Fetch the original image
fetch('http://127.0.0.1:4567/hello2')
// Retrieve its body as ReadableStream
.then(response => {
return response.json();
})
.then(data => {
  console.log(data);
  let count = 1;
  //const obj = JSON.parse(data);
  var table = document.getElementById("dataTable");
  Object.keys(data).forEach(function(key) {
    var row = table.insertRow(count);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    cell1.innerHTML = key;
    cell2.innerHTML = data[key];
    count++;
    console.log('Key : ' + key + ', Value : ' + data[key])
  })
})
*/
