import unittest
from bs4 import BeautifulSoup
import requests

class FindHeader(unittest.TestCase):

   def setUp(self):
       self.main_url = "http://{url}".format(url=url)
       self.req = requests.get(self.main_url)
       self.soup = BeautifulSoup(self.req.text, "html.parser")

   def test_header(self):
       title = self.soup.find("h1")
       self.assertEqual(title.get_text(), "3tier App", 'Incorrect header')

   def tearDown(self):
       pass

if __name__ == '__main__':
   url="web.danieljj.com"
   unittest.main()
