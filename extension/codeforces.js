function copyExamples() {
  var txts = $('div.sample-test pre').toArray().map(e => $(e).html().replace(/<br>|<BR>/g, '\n'));
  const input = txts.filter((v, i) => i % 2 === 0);
  const output = txts.filter((v, i) => i % 2 === 1);
  let txt = "";
  for (let i = 0; i < input.length; i++) {
    txt += "😺in Test"+(i+1)+"\n"+input[i].trim()+"\n"+
        "😺out\n"+output[i].trim()+"\n"+
        "😺end\n\n";
  }
  navigator.clipboard.writeText(txt);
}

var $btn = $('<div title="Copy All" class="input-output-copier">Copy All</div>');
$btn.on('click', e => copyExamples());
$('div.sample-tests div.section-title').append($btn);