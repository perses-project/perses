/*
 * Copyright (C) 2018-2020 University of Waterloo.
 *
 * This file is part of Perses.
 *
 * Perses is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3, or (at your option) any later version.
 *
 * Perses is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Perses; see the file LICENSE.  If not see <http://www.gnu.org/licenses/>.
 */

package org.perses;

import com.beust.jcommander.JCommander;
import org.perses.reduction.ReducerFactory;
import org.perses.reduction.ReductionDriver;
import org.perses.util.DefaultLoggingConfigurations;
import org.perses.version.VersionHelper;

import static com.google.common.base.Preconditions.checkArgument;

public class Main {

  static {
    DefaultLoggingConfigurations.configureLogManager("INFO");
  }

  public static void main(String[] args) {
    new Main(args).run();
  }

  private final JCommander commander;
  private final CommandOptions cmd;

  public Main(String[] args) {
    cmd = new CommandOptions(ReducerFactory.getDefaultReductionAlgName());
    commander = cmd.createJCommander(Main.class);
    commander.parse(args);
    // This method should be called as early as possible, to avoid triggering initialization of
    // logger objects.
    DefaultLoggingConfigurations.configureLogManager(cmd.verbosityFlags.verbosity.toUpperCase());
  }

  public void run() {
    if (cmd.help) {
      commander.usage();
      return;
    }
    if (cmd.verbosityFlags.listVerbosity) {
      DefaultLoggingConfigurations.printAllAllowedLoggingLevels();
      return;
    }

    if (cmd.algorithmControlFlags.listAllReductionAlgorithms) {
      System.out.println("All available reduction algorithms: ");
      System.out.println(ReducerFactory.printAllReductionAlgorithms());
      return;
    }

    if (cmd.version) {
      VersionHelper.printVersionInfo("perses", System.out);
      return;
    }
    cmd.validate();

    checkArgument(
        ReducerFactory.isValidReducerName(cmd.algorithmControlFlags.getReductionAlgorithmName()),
        "Invalid reduction algorithm %s",
        cmd.algorithmControlFlags.getReductionAlgorithmName());

    // TODO: create language registry here.
    try (ReductionDriver driver = new ReductionDriver(cmd)) {
      driver.reduce();
    }
  }
}
