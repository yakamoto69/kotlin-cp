function copyExamples() {
  const $e = $('span.lang-ja pre').filter((i, e) => e.id.startsWith('pre-sample'));
  const txts = $e.toArray().map(e => e.textContent);
  const input = txts.filter((v, i) => i % 2 === 0);
  const output = txts.filter((v, i) => i % 2 === 1);
  let txt = "";
  for (let i = 0; i < input.length; i++) {
    txt += "😺in Test"+(i+1)+"\n"+input[i]+
        "😺out\n"+output[i]+
        "😺end\n\n";
  }
  navigator.clipboard.writeText(txt);
}

const $btn = $('<span class="btn btn-default btn-sm btn-copy">Copy All</span>');
$btn.on('click', e => copyExamples());
$btn.insertAfter($('div.io-style + hr').eq(0));