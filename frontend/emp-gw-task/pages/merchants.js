import Layout from "../components/layout";
import {Suspense} from "react";

export default function Merchants({merchantsData}) {
  return (
      <Layout>
        <h1>Merchants</h1>
        <MerchantTable merchants={merchantsData}/>
      </Layout>
  );
}

export async function getStaticProps() {
  // Get external data from the file system, API, DB, etc.

  const headers = new Headers({
    'Authorization': 'Basic ' + btoa("admin:changeit"),
    'Content-Type': 'application/json', 'Accept': 'application/json',
    'X-XSRF-TOKEN': 'ae9a0bda-5fe0-4d71-9a13-2d1b4fffc368'

  })
  console.log("====================================")
  console.log(headers)
  const merchantsData = await fetch('http://localhost:8080/merchants', {
    method: 'GET',
    headers: headers
  }).then(response => response.json()).catch(error => {console.log(error);
    return []});

  console.log(merchantsData)
  // The value of the `props` key will be
  //  passed to the `Home` component
  return {
    props: {merchantsData}
  }

}

function MerchantTable({merchants}) {
  const rows = [];

  merchants.forEach((merchant) => {
    rows.push(
        <MerchantRow
            merchant={merchant}
            key={merchant.id}
        />
    );
  });
  return (
      <Suspense fallback={<div>Loading...</div>}>
        <table>
          <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Description</th>
            <th>e-mail</th>
            <th>Active</th>
            <th>Total Amount</th>
          </tr>
          </thead>
          <tbody>{rows}</tbody>
        </table>
      </Suspense>

  );
}

function MerchantRow({merchant}, {key}) {

  return (<tr key={key}>
    <td>{merchant.id}</td>
    <td>{merchant.merchantName}</td>
    <td>{merchant.description}</td>
    <td>{merchant.email}</td>
    <td>{merchant.active}</td>
    <td>{merchant.totalTransactionAmount}</td>
  </tr>);
}


