const app = require('express')();
const bodyParser = require('body-parser');

const port = 10043;

app.use(bodyParser.json());

app.post('/', (req, res) => {
  const data = req.body;

  console.log(`Problem name: ${data.name}`);
  console.log(`Problem group: ${data.group}`);

  data.tests.forEach((t, i) => {
    const msg = `
😺in Test${i+1}
${t.input.trim()}
😺out
${t.output.trim()}
😺end
`
    console.log(msg)
  })

  res.sendStatus(200);
});

app.listen(port, err => {
  if (err) {
    console.error(err);
    process.exit(1);
  }

  console.log(`Listening on port ${port}`);
});
