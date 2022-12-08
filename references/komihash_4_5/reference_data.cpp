#include "komihash/komihash.h"

#include <iostream>
#include <iomanip>
#include <random>

using namespace std;

int main(int argc, char *argv[])
{

  mt19937_64 rng(0);

  uint64_t maxSize = 200;
  uint64_t numExamplesPerSize = 10;

  uniform_int_distribution<uint8_t> dist(0, 255);

  for (uint64_t size = 0; size <= maxSize; ++size)
  {
    vector<uint8_t> data(size);
    for (uint64_t i = 0; i < numExamplesPerSize; ++i)
    {
      for (uint64_t k = 0; k < size; ++k)
      {
        data[k] = dist(rng);
      }
      uint64_t seed = rng();

      uint64_t hash0 = komihash((char *)(&data[0]), size, 0);
      uint64_t hash1 = komihash((char *)(&data[0]), size, seed);

      cout << "builder.add(0x";
      cout << hex << setfill('0') << setw(16) << hash0;
      cout << "L, 0x";
      cout << hex << setfill('0') << setw(16) << hash1;
      cout << "L, 0x";
      cout << hex << setfill('0') << setw(16) << seed << 'L';
      cout << ", \"";
      for (uint64_t k = 0; k < size; ++k)
        cout << hex << setfill('0') << setw(2) << static_cast<uint64_t>(data[k]);
      cout << "\");";

      cout << endl;
    }
  }

  // some more special cases
  {
    uint64_t size = 64;
    vector<uint8_t> data(size);
    for (uint64_t s = 0; s < 256; ++s)
    {
      for (uint64_t i = 127; i <= 128; ++i)
      {
        for (uint64_t k = 0; k < size - 1; ++k)
        {
          data[k] = dist(rng);
        }
        data[63] = i;
        uint64_t seed = rng();
        seed = ((seed >> 8) << 8) | s;

        uint64_t hash0 = komihash((char *)(&data[0]), size, 0);
        uint64_t hash1 = komihash((char *)(&data[0]), size, seed);

        cout << "builder.add(0x";
        cout << hex << setfill('0') << setw(16) << hash0;
        cout << "L, 0x";
        cout << hex << setfill('0') << setw(16) << hash1;
        cout << "L, 0x";
        cout << hex << setfill('0') << setw(16) << seed << 'L';
        cout << ", \"";
        for (uint64_t k = 0; k < size; ++k)
          cout << hex << setfill('0') << setw(2) << static_cast<uint64_t>(data[k]);
        cout << "\");";

        cout << endl;
      }
    }
  }
}